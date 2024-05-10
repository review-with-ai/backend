package com.aireview.review.login.usernamepassword;

import com.aireview.review.authentication.jwt.Jwt;
import com.aireview.review.login.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final Jwt jwt;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        String token = jwt.createJwt(user.getUserId(), user.getUsername(), user.getAuthorities());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                objectMapper.writeValueAsString(new LoginResponse(token, user.getUserId()))
        );
        response.getWriter().flush();
        response.getWriter().close();
    }
}
