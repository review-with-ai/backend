package com.aireview.review.authentication.jwt;

import com.aireview.review.authentication.CustomAuthenticatedPrincipal;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final Jwt jwt;


    @Override
    public Authentication authenticate(Authentication authentication) {
        Jwt.Claims claims = null;
        try {
            claims = jwt.verify((String) authentication.getCredentials());
        } catch (JWTVerificationException ex) {
            throw new BadCredentialsException("jwt token is not valid");
        }
        if (claims == null) {
            throw new BadCredentialsException("jwt token is not valid");
        }
        return JwtAuthenticationToken.authenticated(
                new CustomAuthenticatedPrincipal(claims.userkey(), claims.username()),
                AuthorityUtils.createAuthorityList(claims.roles())
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
