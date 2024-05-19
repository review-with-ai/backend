package com.aireview.review.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus(){
        return errorCode.getStatus();
    }
}
