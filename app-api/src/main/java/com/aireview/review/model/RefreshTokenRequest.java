package com.aireview.review.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefreshTokenRequest {
    @NotBlank(message = "refreshToken should be provided")
    private final String refreshToken;

    @NotNull(message = "userId should be provided")
    private final Long userId;
}
