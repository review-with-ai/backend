package com.aireview.review.authentication.jwt;

import lombok.Getter;

@Getter
public class Jwt {
    private String accessToken;
    private String refreshToken;

    public Jwt(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
