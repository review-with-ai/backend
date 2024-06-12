package com.aireview.review.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
@EnableJpaAuditing
public class BeanConfiguration {
    @Bean
    public PasswordEncoder pbk2PasswordEncoder(SecretEncoderConfig config) {
        return new Pbkdf2PasswordEncoder(
                config.getSecret(),
                config.getSaltLength(),
                config.getIteration(),
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
    }
}
