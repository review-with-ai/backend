package com.aireview.review.controller;

import com.aireview.review.authentication.jwt.Jwt;
import com.aireview.review.authentication.jwt.JwtService;
import com.aireview.review.authentication.jwt.exception.RefreshTokenNotFoundException;
import com.aireview.review.config.CookieUtil;
import com.aireview.review.config.swagger.ApiExceptionExample;
import com.aireview.review.config.swagger.docs.RefreshTokenPossibleExceptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "인증")
public class TokenController {

    private final JwtService jwtService;

    @GetMapping("/refresh-token")
    @Operation(summary = "리프레시 토큰 갱신 API", description = "리프레시 토큰(쿠키)로 새로운 액세스 토큰 및 리프레시 토큰 발급(로그인과 같은 역할)")
    @ApiResponse(responseCode = "200", description = "쿠키 발급",
            headers = @Header(name = HttpHeaders.SET_COOKIE, ref = "#/components/headers/JwtSetCookieHeader"))
    @ApiExceptionExample(RefreshTokenPossibleExceptions.class)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.getCookie(request, jwtService.getRefreshTokenName())
                .orElseThrow(() -> RefreshTokenNotFoundException.INSTANCE)
                .getValue();

        Jwt jwt = jwtService.refreshToken(refreshToken);

        CookieUtil.addCookie(response, jwtService.getAccessTokenName(), jwt.getAccessToken(), jwtService.getExpirySeconds());
        CookieUtil.addCookie(response, jwtService.getRefreshTokenName(), jwt.getRefreshToken(), jwtService.getRefreshTokenExpirySeconds());
    }
}
