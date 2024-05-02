package com.aireview.review.config.security;

import lombok.Getter;

@Getter
public class AuthenticatedPrincipal {
    private final Long id;
    private final String email;

    public AuthenticatedPrincipal(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
