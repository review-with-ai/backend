package com.aireview.review.common.exception;

public abstract class ValidationException extends AiReviewException {
    protected ValidationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
