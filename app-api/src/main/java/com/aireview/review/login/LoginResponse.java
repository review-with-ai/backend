package com.aireview.review.login;

import lombok.Getter;

@Getter
public class LoginResponse {
    private final String token;
    private final Long userId;

    public LoginResponse(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }
}
