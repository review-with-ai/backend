package com.aireview.review.login.usernamepassword;

import com.aireview.review.authentication.jwt.JwtConfig;
import com.aireview.review.authentication.jwt.JwtService;
import com.aireview.review.domains.user.domain.User;
import com.aireview.review.login.LoginSuccessHandler;
import com.aireview.review.login.UserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthenticationSuccessHandler extends LoginSuccessHandler {
    public UsernamePasswordAuthenticationSuccessHandler(JwtConfig jwtConfig, JwtService jwtService) {
        super(jwtConfig, jwtService);
    }

    @Override
    protected User processAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
