package com.aireview.review.login;

import com.aireview.review.authentication.jwt.Jwt;
import com.aireview.review.authentication.jwt.JwtService;
import com.aireview.review.domain.user.User;
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
                user.getEmail().getAddress(),
                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new));

        LoginResponse loginResponse = new LoginResponse(jwt.getAccessToken(), jwt.getRefreshToken(), userId);
        writeLoginSuccessResponse(response, loginResponse);
    }

    protected abstract User processAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

    private void writeLoginSuccessResponse(HttpServletResponse response, LoginResponse loginResponse) {
        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                    objectMapper.writeValueAsString(loginResponse)
            );
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException exception) {
            // TODO: 5/9/24  oauth user 정보가 없으면? IllegalArgumentException 어떻게 처리?
            // TODO: 5/9/24  user 정보를 가져오다가 예외가 발생하면 어떻게 처리할 것인가? DataAccessException 어떻게 처리?
        }
    }
}
