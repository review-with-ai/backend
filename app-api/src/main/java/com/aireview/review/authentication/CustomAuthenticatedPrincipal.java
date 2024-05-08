package com.aireview.review.authentication;

import lombok.Getter;
import org.springframework.security.core.AuthenticatedPrincipal;

public record CustomAuthenticatedPrincipal(
        Long id,
        String email
) implements AuthenticatedPrincipal {

    @Override
    public String getName() {
        return id.toString();
    }
}
