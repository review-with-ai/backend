package com.aireview.review.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

public class KakaoOpenFeignConfiguration {
    @Value("${payment.kakao.secret-key}")
    private String secretKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header(HttpHeaders.AUTHORIZATION, "SECRET_KEY " + secretKey);
    }
}
