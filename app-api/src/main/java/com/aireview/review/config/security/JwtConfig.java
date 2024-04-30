package com.aireview.review.config.security;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "jwt.token")
public class JwtConfig {

    private final String header;

    private final String issuer;

    private final String clientSecret;

    private final int expirySeconds;

    @ConstructorBinding
    public JwtConfig(String header, String issuer, String clientSecret, int expirySeconds) {
        this.header = header;
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.expirySeconds = expirySeconds;
    }
}
