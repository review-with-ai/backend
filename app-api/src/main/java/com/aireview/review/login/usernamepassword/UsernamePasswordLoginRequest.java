package com.aireview.review.login.usernamepassword;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsernamePasswordLoginRequest {

    private String email;

    private String password;

    public UsernamePasswordLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
