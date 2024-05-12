package com.aireview.review.authentication.jwt;

import com.aireview.review.authentication.CustomAuthenticatedPrincipal;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtService jwtService;


    @Override
    public Authentication authenticate(Authentication authentication) {
        JwtService.Claims claims = null;
        try {
            claims = jwtService.verify((String) authentication.getCredentials());

        } catch (TokenExpiredException ex) {
            throw new BadCredentialsException("jwt token has expired");
        } catch (JWTVerificationException ex) {
            throw new BadCredentialsException("jwt token is not valid");
        }

        return JwtAuthenticationToken.authenticated(
                new CustomAuthenticatedPrincipal(claims.userkey(), claims.email()),
                AuthorityUtils.createAuthorityList(claims.roles())
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
