package com.aireview.review.login.usernamepassword;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
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

    private final MappingJackson2HttpMessageConverter jsonMessageConverter;

    public JsonUsernamePasswordAuthenticationFilter(
            AuthenticationManager authenticationManager,
            AuthenticationSuccessHandler authenticationSuccessHandler,
            AuthenticationFailureHandler authenticationFailureHandler,
            MappingJackson2HttpMessageConverter jsonMessageConverter
    ) {
        super(ANT_PATH_REQUEST_MATCHER, authenticationManager);
        setAuthenticationSuccessHandler(authenticationSuccessHandler);
        setAuthenticationFailureHandler(authenticationFailureHandler);
        this.jsonMessageConverter = jsonMessageConverter;
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
        return (UsernamePasswordLoginRequest) this.jsonMessageConverter
                .read(UsernamePasswordLoginRequest.class, null, new ServletServerHttpRequest(request));
    }
}
