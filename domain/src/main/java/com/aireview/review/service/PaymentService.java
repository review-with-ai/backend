package com.aireview.review.service;

import com.aireview.review.domains.subscription.Payment;
import com.aireview.review.domains.subscription.PaymentRepository;
import com.aireview.review.domains.subscription.Subscription;
import com.aireview.review.domains.subscription.SubscriptionRepository;
import com.aireview.review.domains.subscription.exception.AlreadySubscribedException;
import com.aireview.review.domains.subscription.exception.PayRequestNotFoundException;
import com.aireview.review.domains.subscription.exception.PaymentApprovalFailException;
import com.aireview.review.service.kakaopay.*;
import feign.FeignException;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private static final int MONTHLY_SUBSCRIPTION_FEE = 1000;

    private static final String MONTHLY_SUBSCRIPTION_NAME = "월구독권";

    private static final String KAKAO_PAYMENT_APPROVAL_PATH = "/api/v1/subscription/kakao/approve";

    private static final String KAKAO_PAYMENT_FAIL_PATH = "/api/v1/subscription/kakao/fail";

    private static final String KAKAO_PAYMENT_CANCEL_PATH = "/api/v1/subscription/kakao/cancel";

    private final UserService userService;

    private final SubscriptionRepository subscriptionRepository;

    private final PaymentRepository paymentRepository;

    private final KakaoPayFeign feign;

    private final RedisTemplate<String, Object> redisTemplate;

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
        if (userService.hasSubscribed(userId)) {
            throw AlreadySubscribedException.INSTANCE;
        }

        String temp_order_id = UUID.randomUUID().toString();

        KakaoPayReadyRequest request = new KakaoPayReadyRequest(
                cid,
                temp_order_id,
                userId.toString(),
                MONTHLY_SUBSCRIPTION_NAME,
                1,
                MONTHLY_SUBSCRIPTION_FEE,
                0,
                getUrlWithTempOrderId(payment_approval_url, temp_order_id),
                getUrlWithTempOrderId(payment_fail_url, temp_order_id),
                getUrlWithTempOrderId(payment_cancel_url, temp_order_id)
        );


        KakaoPayReadyResponse response = feign.ready(request);

        savePayRequest(request, response);

        log.debug(response.toString());

        return response.getNextRedirectPcUrl();
    }

    private String getUrlWithTempOrderId(String url, String tempOrderId) {
        return url + "?order_id=" + tempOrderId;
    }

    private void savePayRequest(KakaoPayReadyRequest request, KakaoPayReadyResponse response) {
        redisTemplate.opsForValue()
                .set(PAY_REQUEST_KEY_PREFIX + request.getPartnerOrderId(),
                        new SavedPayRequest(
                                response.getTid(),
                                request.getPartnerOrderId(),
                                request.getCid(),
                                request.getPartnerUserId(),
                                request.getTotalAmount(),
                                request.getItemName(),
                                response.getCreatedAt()
                        )
                );
    }

    public void approvePayment(String tempOrderId, String pgToken) {
        SavedPayRequest savedPayRequest = retrieveSavedPayRequest(tempOrderId);

        KakaoPayApproveResponse response;
        try {
            response = feign.approve(new KakaoPayApproveRequest(
                    savedPayRequest.getCid(),
                    savedPayRequest.getTid(),
                    savedPayRequest.getTempOrderId(),
                    savedPayRequest.getUserId(),
                    pgToken
            ));
        } catch (FeignException exception) {
            throw PaymentApprovalFailException.INSTANCE;
        }

        Subscription subscription = Subscription.of(Long.parseLong(response.getPartnerUserId()), response.getSid());
        subscriptionRepository.save(subscription);

        Payment payment = new Payment(
                subscription.getId(),
                response.getTid(),
                response.getSid(),
                response.getAmount().getTotal(),
                (byte) 1,
                Payment.EventType.SUCCESS,
                null,
                response.getApprovedAt()
        );
        paymentRepository.save(payment);
        userService.updateSubscriptionStatus(subscription.getUserId(), true);
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
