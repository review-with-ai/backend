package com.aireview.review.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "로그인 혹은 리프레시 토큰 갱신 성공 응답")
public class LoginResponse {
    @Schema(description = "발급된 액세스 토큰. 인증을 위해 사용", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJPQVVUSDJfVVNFUiJdLCJpc3MiOiJhaXJldmlldyIsIm5hbWUiOiJpOTYwMTA3QG5hdmVyLmNvbSIsImV4cCI6MTcxNjQ3OTM0MiwiaWF0IjoxNzE2NDc4NzQyLCJ1c2VyS2V5Ijo1fQ.r6oZsfgO6wgOzZA3fAW_SHHnhmkYi7njaTK2RBSRhII9IBad8gU9Sjmw7Ea9cMjgu776asWbBgCd8LYiGiLdyw")
    private final String accessToken;
    @Schema(description = "발급된 리프레시 토큰. 액세스 토큰 만료시 재발급을 위해 사용", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJPQVVUSDJfVVNFUiJdLCJpc3MiOiJhaXJldmlldyIsIm5hbWUiOiJpOTYwMTA3QG5hdmVyLmNvbSIsImV4cCI6MTcxNzM0Mjc0MiwiaWF0IjoxNzE2NDc4NzQyLCJ1c2VyS2V5Ijo1fQ.5D2bODcNMUMkHJJsmCbkDnW-lN7Bl5AcksQUziNERYSa4XLPKzM7Yp6C_k0ApETmzYVScuw9E9z-bi7FKcUApw")
    private final String refreshToken;
    @Schema(description = "유저 식별자", example = "1")
    private final Long userId;

    public LoginResponse(String accessToken, String refreshToken, Long userId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
}
