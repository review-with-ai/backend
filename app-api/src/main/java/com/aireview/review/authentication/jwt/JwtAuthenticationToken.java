package com.aireview.review.authentication.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private Object principal;

    private Object credentials;

    private JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
    }

    public static JwtAuthenticationToken unauthenticated(Object credentials) {
        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(null, null, credentials);
        authRequest.setAuthenticated(false);
        return authRequest;
    }

    public static JwtAuthenticationToken authenticated(Object principal, Collection<? extends GrantedAuthority> authorities) {
        JwtAuthenticationToken authResult = new JwtAuthenticationToken(authorities, principal, null);
        authResult.setAuthenticated(true);
        return authResult;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }


    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}

