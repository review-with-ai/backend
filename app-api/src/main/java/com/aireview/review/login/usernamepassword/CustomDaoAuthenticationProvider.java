package com.aireview.review.login.usernamepassword;

import com.aireview.review.authentication.CustomAuthenticatedPrincipal;
import com.aireview.review.login.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;

@Deprecated(forRemoval = true)
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, org.springframework.security.core.userdetails.UserDetails user) {
        UserDetails customUser = (UserDetails) user;
        return UsernamePasswordAuthenticationToken.authenticated(
                new CustomAuthenticatedPrincipal(customUser.getUserId(), customUser.getUsername()),
                null,
                user.getAuthorities()
        );
    }
}
