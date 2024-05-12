package com.aireview.review.login.usernamepassword;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String USERNAME_KEY = "username";

    public static final String PASSWORD_KEY = "password";

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
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {
            UsernamePasswordLoginRequest loginRequest = getUsernamePassword(request);
            UsernamePasswordAuthenticationToken unauthenticated = UsernamePasswordAuthenticationToken
                    .unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());

            return super.getAuthenticationManager()
                    .authenticate(unauthenticated);
        } catch (IOException ioException) {
            throw new BadCredentialsException("invalid login request");
        }
    }

    public UsernamePasswordLoginRequest getUsernamePassword(HttpServletRequest request) throws IOException {
        return this.objectMapper.readValue(request.getInputStream(), UsernamePasswordLoginRequest.class);
    }
}
