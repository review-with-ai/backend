package com.aireview.review.login.usernamepassword;

import com.aireview.review.authentication.CustomAuthenticatedPrincipal;
import com.aireview.review.authentication.jwt.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        CustomUserDetails customUser = (CustomUserDetails) user;
        return UsernamePasswordAuthenticationToken.authenticated(
                new CustomAuthenticatedPrincipal(customUser.getUserKey(), customUser.getUsername()),
                null,
                user.getAuthorities()
        );
    }
}
