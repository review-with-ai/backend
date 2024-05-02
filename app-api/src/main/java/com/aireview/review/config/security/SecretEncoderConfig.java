package com.aireview.review.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Component;

import java.beans.ConstructorProperties;

@Getter
@ConfigurationProperties(prefix = "secret.encoder")
public class SecretEncoderConfig {

    private final String secret;

    private final int saltLength;

    private final int iteration;

    @ConstructorBinding
    public SecretEncoderConfig(String secret, int saltLength, int iteration) {
        this.secret = secret;
        this.saltLength = saltLength;
        this.iteration = iteration;
    }
}
