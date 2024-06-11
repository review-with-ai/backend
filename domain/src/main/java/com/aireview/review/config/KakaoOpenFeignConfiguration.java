package com.aireview.review.config;

import com.aireview.review.service.kakaopay.KakaoPayFeign;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
@EnableFeignClients(clients = KakaoPayFeign.class)
public class KakaoOpenFeignConfiguration {


    @Value("${payment.kakao.secret-key}")
    private String secretKey;

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header(HttpHeaders.AUTHORIZATION, "SECRET_KEY " + secretKey); }
}
