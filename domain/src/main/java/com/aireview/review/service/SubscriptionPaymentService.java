package com.aireview.review.service;

import com.aireview.review.common.exception.ResourceNotFoundException;
import com.aireview.review.config.RedisRepository;
import com.aireview.review.domains.subscription.*;
import com.aireview.review.domains.subscription.exception.*;
import com.aireview.review.service.kakaopay.*;
import feign.FeignException;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SubscriptionPaymentService {
    private static final int MONTHLY_SUBSCRIPTION_FEE = 1000;

    private static final String KAKAO_PAYMENT_APPROVAL_PATH = "/api/v1/subscription/payment/approve";

    private static final String KAKAO_PAYMENT_FAIL_PATH = "/api/v1/subscription/payment/fail";

    private static final String KAKAO_PAYMENT_CANCEL_PATH = "/api/v1/subscription/payment/cancel";

    private static final String PAY_REQUEST_KEY_PREFIX = "pay_request:";

    private static final String PRODUCT_NAME_FORMAT = "월구독권[%d회차]";

    private final SubscriptionRepository subscriptionRepository;

    private final PaymentRepository paymentRepository;

    private final KakaoPayFeign feign;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final RedisRepository redisRepository;

    private String payment_approval_url;

    private String payment_fail_url;

    private String payment_cancel_url;

    @Value("${payment.kakao.cid}")
    private String cid;

    @Value("${server.host}")
    private String host;

    @PostConstruct
    private void init() {
        payment_approval_url = host + KAKAO_PAYMENT_APPROVAL_PATH;
        payment_fail_url = host + KAKAO_PAYMENT_FAIL_PATH;
        payment_cancel_url = host + KAKAO_PAYMENT_CANCEL_PATH;
    }


    //1회차 결제
    public String ready(Long userId) {
        // TODO: 6/16/24 취소된 사용권있는지 살펴봐야함.
        Optional<Subscription> subscription = subscriptionRepository
                .findByUserIdAndStatusIn(userId, Set.of(Subscription.Status.ACTIVE, Subscription.Status.CANCEL));

        if (subscription.isPresent()) {
            throw AlreadySubscribedException.INSTANCE;
        }

        String temp_order_id = UUID.randomUUID().toString();
        String temp_user_id = UUID.randomUUID().toString();
        byte seq = 1;

        KakaoPayReadyRequest request = new KakaoPayReadyRequest(
                cid,
                temp_order_id,
                temp_user_id,
                String.format(PRODUCT_NAME_FORMAT, seq),
                1,
                MONTHLY_SUBSCRIPTION_FEE,
                0,
                getUrlWithTempOrderId(payment_approval_url, temp_order_id),
                getUrlWithTempOrderId(payment_fail_url, temp_order_id),
                getUrlWithTempOrderId(payment_cancel_url, temp_order_id)
        );


        KakaoPayReadyResponse response = feign.ready(request);

        savePayRequest(userId, seq, request, response);

        return response.getNextRedirectPcUrl();
    }

    private String getUrlWithTempOrderId(String url, String tempOrderId) {
        return url + "?order_id=" + tempOrderId;
    }

    private void savePayRequest(Long userId, byte seq, KakaoPayReadyRequest request, KakaoPayReadyResponse response) {
        redisRepository.save(
                PAY_REQUEST_KEY_PREFIX + request.getPartnerOrderId(),
                new SavedPayRequest(
                        response.getTid(),
                        request.getPartnerOrderId(),
                        request.getCid(),
                        request.getPartnerUserId(),
                        request.getTotalAmount(),
                        request.getItemName(),
                        response.getCreatedAt(),
                        seq,
                        userId
                ));
    }

    public void approvePayment(String tempOrderId, String pgToken) {
        SavedPayRequest savedPayRequest = retrieveSavedPayRequest(tempOrderId);

        KakaoPayApproveResponse response;
        try {
            response = feign.approve(new KakaoPayApproveRequest(
                    savedPayRequest.getCid(),
                    savedPayRequest.getTid(),
                    savedPayRequest.getTempOrderId(),
                    savedPayRequest.getTempUserId(),
                    pgToken
            ));
        } catch (FeignException exception) {
            throw PaymentApprovalFailException.INSTANCE;
        }

        Long userId = savedPayRequest.getUserId();

        subscriptionRepository.findByUserIdAndStatusIn(userId, Set.of(Subscription.Status.ACTIVE, Subscription.Status.CANCEL))
                .ifPresent(subscription -> {
                    throw AlreadySubscribedException.INSTANCE;
                });

        Subscription newSubscription = Subscription.of(userId, response.getSid(), response.getPartnerUserId());

        subscriptionRepository.save(newSubscription);

        Payment payment = new Payment(
                newSubscription.getId(),
                response.getTid(),
                response.getSid(),
                savedPayRequest.getTempOrderId(),
                response.getAmount().getTotal(),
                savedPayRequest.getSeq(),
                Payment.EventType.SUCCESS,
                null,
                response.getApprovedAt()
        );
        paymentRepository.save(payment);

        applicationEventPublisher.publishEvent(new PaymentSuccessEvent(this, payment));
    }

    private SavedPayRequest retrieveSavedPayRequest(String tempOrderId) {
        return redisRepository
                .getAndDelete(PAY_REQUEST_KEY_PREFIX + tempOrderId, SavedPayRequest.class)
                .orElseThrow(() -> PayRequestNotFoundException.INSTANCE);
    }

    public void deleteSavedPayRequest(String tempOrderId) {
        redisRepository.getAndDelete(PAY_REQUEST_KEY_PREFIX + tempOrderId, SavedPayRequest.class);
    }

    // 2회차 이후 결제
    public void recurringPay(Long userId) {
        Subscription subscription = subscriptionRepository.findByUserIdAndStatus(userId, Subscription.Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(SubscriptionErrorCode.SUBSCRIPTION_NOT_FOUND, "by user id :" + userId));

        if (subscription.isCancelled()) {
            throw CancelledSubscriptionException.INSTANCE;
        }

        if (!subscription.isRenewalDue()) {
            throw new RenewalNotDueException("renewal is required at " + subscription.getRenewalDate().toString());
        }


        byte nxtPaymentSeq = (byte) (paymentRepository.findMaxSeqBySubscriptionId(subscription.getId()) + 1);
        String order_id = UUID.randomUUID().toString();
        KakaoPayRecurringPayRequest request = new KakaoPayRecurringPayRequest(
                cid,
                subscription.getSid(),
                order_id,
                subscription.getPartnerUserId(),
                String.format(PRODUCT_NAME_FORMAT, nxtPaymentSeq),
                MONTHLY_SUBSCRIPTION_FEE
        );

        KakaoPayRecurringPayResponse response = feign.recurringPay(request);

        Payment payment = new Payment(
                subscription.getId(),
                response.getTid(),
                response.getSid(),
                response.getPartnerOrderId(),
                response.getAmount().getTotal(),
                nxtPaymentSeq,
                Payment.EventType.SUCCESS,
                null,
                response.getApprovedAt()
        );
        paymentRepository.save(payment);
        applicationEventPublisher.publishEvent(new PaymentSuccessEvent(this, payment));
    }

    public void inactive(Long userId) {
        Subscription subscription = subscriptionRepository.findByUserIdAndStatusIn(userId, Set.of(Subscription.Status.ACTIVE, Subscription.Status.CANCEL))
                .orElseThrow(() -> new ResourceNotFoundException(SubscriptionErrorCode.SUBSCRIPTION_NOT_FOUND, String.format("by userId %s", userId)));
        if (subscription.isCancelled()) {
            throw CancelledSubscriptionException.INSTANCE;
        }
        KakaoPayInactivateResponse response = feign.invalidateSid(new KakaoPayInactivateRequest(cid, subscription.getSid()));
        subscription.cancel(response.getInactivatedAt());
        // TODO: 6/16/24 배치로 구독주기 마지막날에 만료시켜야함.
    }

}
