package com.aireview.review.common.exception;

public class AccessDeniedException extends AiReviewException {
    public AccessDeniedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
