package com.aireview.review.model;

import com.aireview.review.common.exception.ErrorCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final String code;

    private final String reason;

    private final LocalDateTime timestamp;

    private final String path;

    public ErrorResponse(ErrorCode errorCode, String path) {
        this.code = errorCode.getCode();
        this.reason = errorCode.getReason();
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }

    public ErrorResponse(String code, String reason, String path) {
        this.code = code;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }
}
