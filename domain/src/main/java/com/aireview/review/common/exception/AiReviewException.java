package com.aireview.review.common.exception;

import lombok.Getter;

@Getter
public abstract class AiReviewException extends RuntimeException {
    private ErrorCode errorCode;

    protected AiReviewException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    protected AiReviewException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
