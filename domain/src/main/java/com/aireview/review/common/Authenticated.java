package com.aireview.review.common;

import lombok.Getter;

@Getter
public class Authenticated {
    private final Long userId;
    private final String email;

    public Authenticated(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }
}
