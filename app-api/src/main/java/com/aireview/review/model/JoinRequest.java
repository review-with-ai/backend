package com.aireview.review.model;

import com.aireview.review.domain.user.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class JoinRequest {

    @NotBlank(message = "name should be provided")
    private final String name;

    @NotBlank(message = "nickname should be provided")
    private final String nickname;

    @NotNull(message = "email should be provided")
    private final Email email;

    @NotBlank(message = "password should be provided")
    private final String password;

    public JoinRequest(String name, String nickname, Email email, String password) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
