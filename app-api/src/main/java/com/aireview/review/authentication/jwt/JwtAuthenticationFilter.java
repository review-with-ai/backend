package com.aireview.review.authentication.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


@Getter
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final String AUTHENTICATION_SCHEME_BEARER = "BEARER";

    private static final int AUTHENTICATION_SCHEME_BEARER_LENGTH = 6;

    private final String headerKey;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(String headerKey, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.headerKey = headerKey;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        Assert.notNull(this.headerKey, "HeaderKey is required");
        Assert.notNull(this.jwtService, "jwt is required ");
        Assert.notNull(this.authenticationManager, "authenticationManager is required");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        JwtAuthenticationToken authRequest = extract(request);
        if (authRequest == null) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authResult = this.getAuthenticationManager().authenticate(authRequest);

        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    public JwtAuthenticationToken extract(ServletRequest request) throws ServletException {
        if (!(request instanceof HttpServletRequest httpRequest)) {
            throw new ServletException("JwtAuthenticationFilter only supports HTTP request");
        }
        String header = httpRequest.getHeader(headerKey);
        if (header == null) {
            return null;
        }
        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_BEARER)) {
            return null;
        }
        if (header.equalsIgnoreCase(AUTHENTICATION_SCHEME_BEARER)) {
            throw new BadCredentialsException("Empty jwt authentication token");
        }
        return JwtAuthenticationToken.unauthenticated(header.substring(AUTHENTICATION_SCHEME_BEARER_LENGTH + 1));
    }

}
