package com.aireview.review.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.concurrent.Executor;

@org.springframework.context.annotation.Configuration
@EnableJpaAuditing
@EnableAsync
public class Configuration implements AsyncConfigurer {
    @Bean
    public PasswordEncoder pbk2PasswordEncoder(SecretEncoderConfig config) {
        return new Pbkdf2PasswordEncoder(
                config.getSecret(),
                config.getSaltLength(),
                config.getIteration(),
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor asyncExecutor = new ThreadPoolTaskExecutor();
        asyncExecutor.setThreadNamePrefix("async-event-handler");
        asyncExecutor.setCorePoolSize(2);
        asyncExecutor.setMaxPoolSize(2);
        asyncExecutor.setQueueCapacity(10);
        asyncExecutor.setWaitForTasksToCompleteOnShutdown(true);
        asyncExecutor.setAwaitTerminationSeconds(30);
        asyncExecutor.initialize();
        return asyncExecutor;
    }
}
