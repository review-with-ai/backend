package com.aireview.review.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefreshTokenRequest {
    @NotBlank(message = "refreshToken should be provided")
    @Schema(description = "이전에 발급받은 리프레시 토큰", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJPQVVUSDJfVVNFUiJdLCJpc3MiOiJhaXJldmlldyIsIm5hbWUiOiJpOTYwMTA3QG5hdmVyLmNvbSIsImV4cCI6MTcxNzM0Mjc0MiwiaWF0IjoxNzE2NDc4NzQyLCJ1c2VyS2V5Ijo1fQ.5D2bODcNMUMkHJJsmCbkDnW-lN7Bl5AcksQUziNERYSa4XLPKzM7Yp6C_k0ApETmzYVScuw9E9z-bi7FKcUApw")
    private final String refreshToken;

    @Schema(description = "유저 식별자", example = "1")
    @NotNull(message = "userId should be provided")
    private final Long userId;
}
