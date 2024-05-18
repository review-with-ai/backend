package com.aireview.review.authentication.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.util.Assert;

@Getter
@ConfigurationProperties(prefix = "jwt.token")
public class JwtConfig {

    private final String header;

    private final String issuer;

    private final String clientSecret;

    private final int expirySeconds;

    private final int refreshTokenExpirySeconds;

    @ConstructorBinding
    public JwtConfig(String header, String issuer, String clientSecret, int expirySeconds, int refreshTokenExpirySeconds) {
        Assert.notNull(issuer, "issuer must not be null");
        Assert.notNull(clientSecret, "client secret must not be null");
        Assert.isTrue(expirySeconds > 0, "expirySeconds should be greater than 0");
        Assert.isTrue(refreshTokenExpirySeconds > 0 &&
                        refreshTokenExpirySeconds > expirySeconds,
                "refresh token expiration should be longer than access token expiration");
        this.header = header;
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.expirySeconds = expirySeconds;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
    }
}
