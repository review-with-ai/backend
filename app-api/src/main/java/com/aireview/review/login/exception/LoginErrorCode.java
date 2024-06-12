package com.aireview.review.login.exception;

import com.aireview.review.common.HttpStatus;
import com.aireview.review.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum LoginErrorCode implements ErrorCode {
    WRONG_EMAIL(HttpStatus.UNAUTHORIZED, "LOGIN_401_1", "존재하지 않는 이메일 입니다."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "LOGIN_401_2", "잘못된 비밀번호 입니다."),
    OAUTH_FAIL(HttpStatus.UNAUTHORIZED, "LOGIN_401_3", "oauth 인증 실패"),
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "LOGIN_401_5", "로그인 실패"),
    WRONG_LOGIN_REQUEST_FORMAT(HttpStatus.BAD_REQUEST, "LOGIN_401_6", "잘못된 로그인 형식입니다.");

    private final int status;
    private final String code;
    private final String reason;

    LoginErrorCode(int status, String code, String reason) {
        this.status = status;
        this.code = code;
        this.reason = reason;
    }
}
