package com.aireview.review.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class JoinRequest {

    @NotBlank(message = "name should be provided")
    private final String name;

    @NotBlank(message = "nickname should be provided")
    private final String nickname;

    @NotBlank(message = "email should be provided")
    private final String email;

    @NotBlank(message = "password should be provided")
    private final String password;

    public JoinRequest(String name, String nickname, String email, String password) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

}
