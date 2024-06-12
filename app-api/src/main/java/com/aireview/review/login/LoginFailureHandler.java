package com.aireview.review.login;

import com.aireview.review.common.exception.ErrorCode;
import com.aireview.review.login.exception.LoginErrorCode;
import com.aireview.review.login.exception.LoginRequestFormatException;
import com.aireview.review.model.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ErrorCode errorCode;
        if (exception instanceof OAuth2AuthenticationException) {
            errorCode = LoginErrorCode.OAUTH_FAIL;
        } else if (exception instanceof UsernameNotFoundException) {
            errorCode = LoginErrorCode.WRONG_EMAIL;
        } else if (exception instanceof BadCredentialsException) {
            errorCode = LoginErrorCode.WRONG_PASSWORD;
        } else if (exception instanceof LoginRequestFormatException) {
            errorCode = LoginErrorCode.WRONG_LOGIN_REQUEST_FORMAT;
        } else {
            errorCode = LoginErrorCode.LOGIN_FAIL;
        }

        ErrorResponse errorResponse = new ErrorResponse(errorCode, request.getRequestURL().toString());
        response.setStatus(errorCode.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
