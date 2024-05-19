package com.aireview.review.model;


import lombok.Getter;

@Deprecated
@Getter
public class LoginResponse {
    private final String token;
    private final Long userId;

    public LoginResponse(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }
}
