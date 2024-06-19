package com.aireview.review.authentication.jwt;

import com.aireview.review.common.Authenticated;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final String ACCESS_TOKEN_EXPIRED_MESSAGE = "액세스 토큰이 만료되었습니다.";

    private static final String ACCESS_TOKEN_VERIFICATION_MESSAGE = "유효하지 않은 토큰입니다.";

    private final JwtService jwtService;


    @Override
    public Authentication authenticate(Authentication authentication) {
        JwtService.Claims claims = null;
        try {
            claims = jwtService.verify((String) authentication.getCredentials());
        } catch (TokenExpiredException ex) {
            throw new BadCredentialsException(ACCESS_TOKEN_EXPIRED_MESSAGE);
        } catch (JWTVerificationException ex) {
            throw new BadCredentialsException(ACCESS_TOKEN_VERIFICATION_MESSAGE);
        }

        return JwtAuthenticationToken.authenticated(
                new Authenticated(claims.userkey(), claims.email()),
                AuthorityUtils.createAuthorityList(claims.roles())
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
