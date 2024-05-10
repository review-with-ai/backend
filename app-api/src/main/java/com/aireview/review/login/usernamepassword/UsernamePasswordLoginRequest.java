package com.aireview.review.login.usernamepassword;

import lombok.Getter;

@Getter
public class UsernamePasswordLoginRequest {
    private final String username;
    private final String password;

    public UsernamePasswordLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
