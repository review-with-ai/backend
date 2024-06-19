package com.aireview.review.authentication.jwt;

import com.aireview.review.config.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Getter
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String accessTokenCookieName;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final List<RequestMatcher> excludeRequestMatcher;

    public JwtAuthenticationFilter(
            String accessTokenCookieName,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            List<RequestMatcher> excludeRequestMatcher) {
        this.accessTokenCookieName = accessTokenCookieName;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.excludeRequestMatcher = excludeRequestMatcher;
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        Assert.notNull(this.accessTokenCookieName, "access token cookie name is required");
        Assert.notNull(this.jwtService, "jwt is required ");
        Assert.notNull(this.authenticationManager, "authenticationManager is required");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if (isAuthenticationRequest(request)) {
            Optional<Cookie> authCookie = extract(request);
            if (authCookie.isEmpty()) {
                return;
            }
            JwtAuthenticationToken authRequest = JwtAuthenticationToken.unauthenticated(authCookie.get().getValue());

            Authentication authResult = this.getAuthenticationManager().authenticate(authRequest);

            SecurityContextHolder.getContext().setAuthentication(authResult);
        }
        chain.doFilter(request, response);
    }

    private boolean isAuthenticationRequest(HttpServletRequest request) {
        boolean excluded = excludeRequestMatcher.stream()
                .anyMatch(requestMatcher -> requestMatcher.matches(request));
        return request.getCookies() != null &&
                CookieUtil.containsCookie(request, accessTokenCookieName) &&
                !excluded;
    }

    public Optional<Cookie> extract(HttpServletRequest request) {
        return CookieUtil.getCookie(request, accessTokenCookieName);
    }

}
