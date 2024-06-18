package com.aireview.review.authentication.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.util.Assert;

@Getter
@ConfigurationProperties(prefix = "jwt.token")
public class JwtConfig {

    private final String accessTokenName;

    private final String issuer;

    private final String clientSecret;

    private final int expirySeconds;

    private final String refreshTokenName;

    private final int refreshTokenExpirySeconds;


    @ConstructorBinding
    public JwtConfig(String accessTokenName, String issuer, String clientSecret, int expirySeconds, String refreshTokenName, int refreshTokenExpirySeconds) {
        Assert.notNull(accessTokenName, "access token name must not be null");
        Assert.notNull(issuer, "issuer must not be null");
        Assert.notNull(clientSecret, "client secret must not be null");
        Assert.isTrue(expirySeconds > 0, "expirySeconds should be greater than 0");
        Assert.isTrue(refreshTokenExpirySeconds > 0 &&
                        refreshTokenExpirySeconds > expirySeconds,
                "refresh token expiration should be longer than access token expiration");
        Assert.notNull(refreshTokenName, "refresh token name must not be null");
        this.accessTokenName = accessTokenName;
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.expirySeconds = expirySeconds;
        this.refreshTokenName = refreshTokenName;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
    }
}
