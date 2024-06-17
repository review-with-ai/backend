package com.aireview.review.config;

import com.aireview.review.service.kakaopay.KakaoPayFeign;
import com.aireview.review.service.slack.SlackNotificationFeign;
import feign.Logger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {KakaoPayFeign.class, SlackNotificationFeign.class})
public class FeignConfiguration {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
