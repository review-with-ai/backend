package com.aireview.review.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

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
