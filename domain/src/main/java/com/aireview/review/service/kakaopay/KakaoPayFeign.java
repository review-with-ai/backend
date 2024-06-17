package com.aireview.review.service.kakaopay;

import com.aireview.review.config.KakaoOpenFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kakaoPayFeign", url = "${payment.kakao.host}", configuration = KakaoOpenFeignConfiguration.class)
public interface KakaoPayFeign {

    @PostMapping("/online/v1/payment/ready")
    KakaoPayReadyResponse ready(
            @RequestBody KakaoPayReadyRequest kakaoPayReadyRequest
    );

    @PostMapping("/online/v1/payment/approve")
    KakaoPayApproveResponse approve(
            @RequestBody KakaoPayApproveRequest kakaoPayApproveRequest
    );

    @PostMapping("/online/v1/payment/subscription")
    KakaoPayRecurringPayResponse recurringPay(
            @RequestBody KakaoPayRecurringPayRequest recurringPayRequest
    );

    @PostMapping("/online/v1/payment/manage/subscription/inactive")
    KakaoPayInactivateResponse invalidateSid(KakaoPayInactivateRequest request);
}
