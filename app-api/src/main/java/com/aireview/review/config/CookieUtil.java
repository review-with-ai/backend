package com.aireview.review.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;
import java.util.Optional;

public class CookieUtil {
    public static void addCookie(HttpServletResponse response, String name, String value, int expiry) {
        // TODO: 6/17/24 samesite 속성 추가 필요 없음? 프론트, 백 서브도메인으로 구분시 ,https후 secure 필요
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .domain("")
                .httpOnly(true)
                .maxAge(expiry)
                .secure(false)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static boolean containsCookie(HttpServletRequest request, String name) {
        return Arrays.stream(request.getCookies()).anyMatch(cookie -> cookie.getName().equals(name));
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny();
    }
}

