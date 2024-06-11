package com.aireview.review.service.kakaopay;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kakaoPayFeign", url = "${payment.kakao.host}")
public interface KakaoPayFeign {

    @PostMapping("/online/v1/payment/ready")
    KakaoPayReadyResponse ready(
            @RequestBody KakaoPayReadyRequest kakaoPayReadyRequest
    );

    @PostMapping("/online/v1/payment/approve")
    KakaoPayApproveResponse approve(
            @RequestBody KakaoPayApproveRequest kakaoPayApproveRequest
    );
}
