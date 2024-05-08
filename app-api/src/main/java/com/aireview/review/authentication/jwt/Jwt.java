package com.aireview.review.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;

@Component
@Getter
public class Jwt {

    private final String issuer;

    private final String clientSecret;

    private final int expirySeconds;

    private final Algorithm algorithm;

    private final JWTVerifier jwtVerifier;

    public Jwt(JwtConfig config) {
        Assert.notNull(config.getIssuer(), "issuer must not be null");
        Assert.notNull(config.getClientSecret(), "client secret must not be null");
        Assert.isTrue(config.getExpirySeconds() > 0, "expirySeconds should be greater than 0");

        this.issuer = config.getIssuer();
        this.clientSecret = config.getClientSecret();
        this.expirySeconds = config.getExpirySeconds();
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    public String create(Claims claims) {
        Date now = new Date();
        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + expirySeconds * 1_000))
                .withClaim("userKey", claims.userKey)
                .withClaim("name", claims.name)
                .withArrayClaim("roles", claims.roles)
                .sign(algorithm);
    }

    public Claims verify(String token) {
        return new Claims(jwtVerifier.verify(token));
    }

    public static class Claims {
        private Long userKey;
        private String name;
        private String[] roles;
        private Date iat;
        private Date exp;

        private Claims() {
        }


        Claims(DecodedJWT decodedJWT) {
            Claim userKey = decodedJWT.getClaim("userKey");
            this.userKey = userKey != null ? userKey.asLong() : null;

            Claim name = decodedJWT.getClaim("name");
            this.name = name != null ? name.asString() : null;

            Claim roles = decodedJWT.getClaim("roles");
            this.roles = roles != null ? roles.asArray(String.class) : null;

            this.iat = decodedJWT.getIssuedAt();
            this.exp = decodedJWT.getExpiresAt();
        }

        public static Claims of(long userKey, String name, String[] roles) {
            Claims claims = new Claims();
            claims.userKey = userKey;
            claims.name = name;
            claims.roles = roles;
            return claims;
        }

        long iat() {
            return iat != null ? iat.getTime() : -1;
        }

        long exp() {
            return exp != null ? exp.getTime() : -1;
        }

        String username() {
            return name;
        }

        Long userkey() {
            return userKey;
        }

        String[] roles() {
            return roles;
        }
    }

}
