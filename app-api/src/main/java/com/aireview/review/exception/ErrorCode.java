package com.aireview.review.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 인증 관련 에러(4100번대)
    REFRESH_TOKEN_EXPIRED_ERROR(HttpStatus.BAD_REQUEST, 4005, "만료된 refresh token입니다. 재로그인이 필요합니다."),
    REFRESH_TOKEN_VERIFICATION_ERROR(HttpStatus.BAD_REQUEST, 4006, "잘못된 refresh token입니다. 재로그인이 필요합니다."),
    // 잘못된 요청(4000번대)
    RESOURCE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, 4000, "존재하지 않는 자원입니다."),
    USERNAME_DUPLICATE_ERROR(HttpStatus.BAD_REQUEST, 4001, "중복된 이메일입니다.");

    private HttpStatus status;
    private int code;
    private String message;

    ErrorCode(HttpStatus status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
