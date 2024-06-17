package com.aireview.review.common.exception;

public abstract class ValidationException extends AiReviewException {
    protected ValidationException(ErrorCode errorCode) {
        super(errorCode);
    }

    protected ValidationException(ErrorCode errorCode,String message) {
        super(errorCode, message);
    }
}
