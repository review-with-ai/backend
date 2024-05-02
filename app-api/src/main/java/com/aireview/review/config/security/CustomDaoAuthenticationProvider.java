package com.aireview.review.config.security;

import com.aireview.review.model.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected Authentication createSuccessAuthentication(java.lang.Object principal, Authentication authentication, UserDetails user) {
        CustomUserDetails customUser = (CustomUserDetails) user;
        return UsernamePasswordAuthenticationToken.authenticated(
                new AuthenticatedPrincipal(customUser.getUserKey(), customUser.getUsername()),
                null,
                user.getAuthorities()
        );
    }
}
