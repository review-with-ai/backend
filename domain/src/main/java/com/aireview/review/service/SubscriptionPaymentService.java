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

import java.time.LocalDate;
import java.util.Optional;
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
        Optional<Subscription> subscription = subscriptionRepository.findByUserIdAndStatus(userId, Subscription.Status.ACTIVE);

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

        subscriptionRepository.findByUserIdAndStatus(userId, Subscription.Status.ACTIVE)
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
                response.getApprovedAt(),
                newSubscription.getUserId()
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
    public void recurringPay(Long userId, Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(SubscriptionErrorCode.SUBSCRIPTION_NOT_FOUND, subscriptionId.toString()));

        if (!subscription.getUserId().equals(userId)) {
            throw new NotUserSubscriptionException("subscription " + subscriptionId + "is not owned by user " + userId);
        }

        LocalDate dueDate = subscription.getStartDate().plusMonths(1).toLocalDate();
        if (!LocalDate.now().equals(dueDate)) {
            throw new RenewalNotDueException("renewal requires at " + subscription.getEndDate().minusDays(1));
        }

        byte nxtPaymentSeq = (byte) (paymentRepository.findMaxSeqBySubscriptionId(subscriptionId).byteValue() + 1);
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
                subscriptionId,
                response.getTid(),
                response.getSid(),
                response.getPartnerOrderId(),
                response.getAmount().getTotal(),
                nxtPaymentSeq,
                Payment.EventType.SUCCESS,
                null,
                response.getApprovedAt(),
                userId
        );
        paymentRepository.save(payment);
        applicationEventPublisher.publishEvent(new PaymentSuccessEvent(this, payment));
    }

}
