package com.aireview.review.service;

import com.aireview.review.domains.subscription.*;
import com.aireview.review.domains.subscription.exception.PayRequestNotFoundException;
import com.aireview.review.domains.subscription.exception.PaymentApprovalFailException;
import com.aireview.review.service.kakaopay.*;
import feign.FeignException;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SubscriptionPaymentService {
    private static final int MONTHLY_SUBSCRIPTION_FEE = 1000;

    private static final String MONTHLY_SUBSCRIPTION_NAME = "월구독권";

    private static final String KAKAO_PAYMENT_APPROVAL_PATH = "/api/v1/subscription/kakao/approve";

    private static final String KAKAO_PAYMENT_FAIL_PATH = "/api/v1/subscription/kakao/fail";

    private static final String KAKAO_PAYMENT_CANCEL_PATH = "/api/v1/subscription/kakao/cancel";

    private final SubscriptionRepository subscriptionRepository;

    private final PaymentRepository paymentRepository;

    private final KakaoPayFeign feign;

    private final RedisTemplate<String, Object> redisTemplate;

    private final ApplicationEventPublisher applicationEventPublisher;

    private String payment_approval_url;

    private String payment_fail_url;

    private String payment_cancel_url;

    private static final String PAY_REQUEST_KEY_PREFIX = "pay_request:";

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


    public String ready(Long userId) {
        Optional<Subscription> subscription = subscriptionRepository.findByUserId(userId);

        byte seq;
        if (subscription.isPresent()) {
            byte lastSeq = paymentRepository.findMaxSeqBySubscriptionId(subscription.get().getId()).byteValue();
            seq = (byte) (lastSeq + 1);
        } else {
            seq = 1;
        }

        String temp_order_id = UUID.randomUUID().toString();
        String temp_user_id = UUID.randomUUID().toString();

        KakaoPayReadyRequest request = new KakaoPayReadyRequest(
                cid,
                temp_order_id,
                temp_user_id,
                String.format("%s[%s회차]", MONTHLY_SUBSCRIPTION_NAME, seq),
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
        redisTemplate.opsForValue()
                .set(PAY_REQUEST_KEY_PREFIX + request.getPartnerOrderId(),
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

        Subscription subscription = subscriptionRepository.findById(userId)
                .orElseGet(() -> Subscription.of(userId, response.getSid()));

        subscriptionRepository.save(subscription);

        Payment payment = new Payment(
                subscription.getId(),
                response.getTid(),
                response.getSid(),
                response.getAmount().getTotal(),
                savedPayRequest.getSeq(),
                Payment.EventType.SUCCESS,
                null,
                response.getApprovedAt()
        );
        paymentRepository.save(payment);

        applicationEventPublisher.publishEvent(new PaymentSuccessEvent(this, userId, payment));
    }

    private SavedPayRequest retrieveSavedPayRequest(String tempOrderId) {
        Object object = redisTemplate.opsForValue()
                .getAndDelete(PAY_REQUEST_KEY_PREFIX + tempOrderId);

        if (!(object instanceof SavedPayRequest)) {
            throw PayRequestNotFoundException.INSTANCE;
        }
        return (SavedPayRequest) object;
    }

    public void deleteSavedPayRequest(String tempOrderId) {
        Object object = redisTemplate.opsForValue()
                .getAndDelete(PAY_REQUEST_KEY_PREFIX + tempOrderId);
    }

}
