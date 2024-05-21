package com.aireview.review.authentication.jwt;

import com.aireview.review.exception.RefreshTokenNotValidException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter
public class JwtService {
    private static final String REFRESH_TOKEN_EXPIRED_ERROR = "만료된 refresh token입니다. 재로그인이 필요합니다.";
    private static final String REFRESH_TOKEN_VERIFICATION_ERROR = "발급된 refresh token이 없거나 발급된 토큰과 다릅니다. 재로그인이 필요합니다.";
    private static final String WRONG_TOKEN_ERROR = "어플리케이션에서 발급된 토큰이 아닙니다.";

    private final String issuer;

    private final String clientSecret;

    private final int expirySeconds;

    private final int refreshTokenExpirySeconds;

    private final Algorithm algorithm;

    private final JWTVerifier jwtVerifier;

    private final RefreshTokenRepository refreshTokenRepository;


    public JwtService(JwtConfig config, RefreshTokenRepository refreshTokenRepository) {
        this.issuer = config.getIssuer();
        this.clientSecret = config.getClientSecret();
        this.expirySeconds = config.getExpirySeconds();
        this.refreshTokenExpirySeconds = config.getRefreshTokenExpirySeconds();
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public Jwt createJwt(Long userId, String email, String[] roles) {
        Claims claims = Claims.of(
                userId,
                email,
                roles
        );
        String accessToken = createAccessToken(claims);
        String refreshToken = createRefreshToken(claims);
        refreshTokenRepository.save(userId, refreshToken);
        return new Jwt(accessToken, refreshToken);
    }

    public Jwt refreshToken(Long userId, String refreshToken) {
        try {
            Claims claims = verify(refreshToken);
            String originalRefreshToken = refreshTokenRepository.findById(userId)
                    .orElseThrow(() -> new RefreshTokenNotValidException(REFRESH_TOKEN_VERIFICATION_ERROR));
            if (!originalRefreshToken.equals(refreshToken)) {
                deleteRefreshToken(userId);
                throw new RefreshTokenNotValidException(REFRESH_TOKEN_VERIFICATION_ERROR);
            }
            return createJwt(claims.userKey, claims.email, claims.roles);
        } catch (TokenExpiredException expiredException) {
            throw new RefreshTokenNotValidException(REFRESH_TOKEN_EXPIRED_ERROR);
        }
    }


    private String createAccessToken(Claims claims) {
        Date now = new Date();
        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + expirySeconds * 1_000))
                .withClaim("userKey", claims.userKey)
                .withClaim("name", claims.email)
                .withArrayClaim("roles", claims.roles)
                .sign(algorithm);
    }

    private String createRefreshToken(Claims claims) {
        Date now = new Date();
        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirySeconds * 1_000))
                .withClaim("userKey", claims.userKey)
                .withClaim("name", claims.email)
                .withArrayClaim("roles", claims.roles)
                .sign(algorithm);
    }

    public Claims verify(String token) {
        try {
            return new Claims(jwtVerifier.verify(token));
        } catch (JWTVerificationException exception) {
            throw new RefreshTokenNotValidException(WRONG_TOKEN_ERROR);
        }
    }

    public void deleteRefreshToken(Long userId) {
        refreshTokenRepository.delete(userId);
    }

    public static class Claims {
        private Long userKey;
        private String email;
        private String[] roles;
        private Date iat;
        private Date exp;

        private Claims() {
        }


        Claims(DecodedJWT decodedJWT) {
            Claim userKey = decodedJWT.getClaim("userKey");
            this.userKey = userKey != null ? userKey.asLong() : null;

            Claim name = decodedJWT.getClaim("email");
            this.email = name != null ? name.asString() : null;

            Claim roles = decodedJWT.getClaim("roles");
            this.roles = roles != null ? roles.asArray(String.class) : null;

            this.iat = decodedJWT.getIssuedAt();
            this.exp = decodedJWT.getExpiresAt();
        }

        public static Claims of(long userKey, String name, String[] roles) {
            Claims claims = new Claims();
            claims.userKey = userKey;
            claims.email = name;
            claims.roles = roles;
            return claims;
        }

        long iat() {
            return iat != null ? iat.getTime() : -1;
        }

        long exp() {
            return exp != null ? exp.getTime() : -1;
        }

        String email() {
            return email;
        }

        Long userkey() {
            return userKey;
        }

        String[] roles() {
            return roles;
        }
    }

}
