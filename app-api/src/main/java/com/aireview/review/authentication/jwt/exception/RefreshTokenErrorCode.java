package com.aireview.review.authentication.jwt.exception;

import com.aireview.review.common.HttpStatus;
import com.aireview.review.common.exception.ErrorCode;
import lombok.Getter;


@Getter
public enum RefreshTokenErrorCode implements ErrorCode {

    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN_401_1", "만료된 리프레시토큰입니다. 재로그인이 필요합니다."),
    TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "TOKEN_401_2", "유저에게 발급된 리프레시 토큰이 없거나 발급된 토큰과 다릅니다. 재로그인이 필요합니다."),
    TOKEN_NOT_ISSUED_BY_APP(HttpStatus.UNAUTHORIZED, "TOKEN_401_3", "ai review에서 발급된 토큰이 아닙니다.");

    private final int status;
    private final String code;
    private final String reason;

    RefreshTokenErrorCode(int status, String code, String reason) {
        this.status = status;
        this.code = code;
        this.reason = reason;
    }
}
