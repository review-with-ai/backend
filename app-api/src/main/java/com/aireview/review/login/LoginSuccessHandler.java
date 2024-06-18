package com.aireview.review.login;

import com.aireview.review.authentication.jwt.Jwt;
import com.aireview.review.authentication.jwt.JwtConfig;
import com.aireview.review.authentication.jwt.JwtService;
import com.aireview.review.config.CookieUtil;
import com.aireview.review.domains.user.domain.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtConfig jwtConfig;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = processAuthenticationSuccess(request, response, authentication);

        Long userId = user.getId();
        Jwt jwt = jwtService.createJwt(
                userId,
                user.getEmail(),
                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new));

        setJwtCookie(response, jwt);
        response.sendRedirect("//ai-review.site/home");
    }

    protected abstract User processAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication);


    private void setJwtCookie(HttpServletResponse response, Jwt jwt) {
        CookieUtil.addCookie(response, "jwt", jwt.getAccessToken(), jwtConfig.getExpirySeconds());
        CookieUtil.addCookie(response, "refresh-token", jwt.getRefreshToken(), jwtConfig.getRefreshTokenExpirySeconds());
    }
}
