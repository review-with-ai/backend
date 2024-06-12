package com.aireview.review.login;

import com.aireview.review.authentication.jwt.Jwt;
import com.aireview.review.authentication.jwt.JwtService;
import com.aireview.review.domains.user.domain.User;
import com.aireview.review.model.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class LoginSuccessHandler implements AuthenticationSuccessHandler {
    protected final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = processAuthenticationSuccess(request, response, authentication);

        Long userId = user.getId();
        Jwt jwt = jwtService.createJwt(
                userId,
                user.getEmail(),
                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new));

        LoginResponse loginResponse = new LoginResponse(jwt.getAccessToken(), jwt.getRefreshToken(), userId);
        writeLoginSuccessResponse(response, loginResponse);
    }

    protected abstract User processAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

    private void writeLoginSuccessResponse(HttpServletResponse response, LoginResponse loginResponse) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                objectMapper.writeValueAsString(loginResponse)
        );
        response.getWriter().flush();
        response.getWriter().close();
    }
}
