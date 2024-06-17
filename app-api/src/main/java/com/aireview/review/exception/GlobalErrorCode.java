package com.aireview.review.exception;


import com.aireview.review.common.HttpStatus;
import com.aireview.review.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum GlobalErrorCode implements ErrorCode {
    REQUEST_NOT_VALID(HttpStatus.BAD_REQUEST, "GLOBAL_400_1", "요청 검증 오류");

    private final int status;
    private final String code;
    private final String reason;

    GlobalErrorCode(int status, String code, String reason) {
        this.status = status;
        this.code = code;
        this.reason = reason;
    }
}
