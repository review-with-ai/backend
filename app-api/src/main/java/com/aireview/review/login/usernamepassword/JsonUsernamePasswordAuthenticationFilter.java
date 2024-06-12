package com.aireview.review.login.usernamepassword;

import com.aireview.review.login.exception.LoginRequestFormatException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final AntPathRequestMatcher ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/api/v1/login", HttpMethod.POST.name());

    private final ObjectMapper objectMapper;

    public JsonUsernamePasswordAuthenticationFilter(
            AuthenticationManager authenticationManager,
            AuthenticationSuccessHandler authenticationSuccessHandler,
            AuthenticationFailureHandler authenticationFailureHandler,
            ObjectMapper objectMapper
    ) {
        super(ANT_PATH_REQUEST_MATCHER, authenticationManager);
        setAuthenticationSuccessHandler(authenticationSuccessHandler);
        setAuthenticationFailureHandler(authenticationFailureHandler);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordLoginRequest loginRequest = getUsernamePassword(request);
        UsernamePasswordAuthenticationToken unauthenticated = UsernamePasswordAuthenticationToken
                .unauthenticated(
                        loginRequest.getEmail(),
                        loginRequest.getPassword());

        return super.getAuthenticationManager()
                .authenticate(unauthenticated);
    }

    public UsernamePasswordLoginRequest getUsernamePassword(HttpServletRequest request) throws LoginRequestFormatException {
        try {
            return this.objectMapper.readValue(request.getInputStream(), UsernamePasswordLoginRequest.class);
        } catch (IOException exception) {
            throw LoginRequestFormatException.INSTANCE;
        }
    }
}
