package com.aireview.review.common.exception;

public class ResourceNotFoundException extends AiReviewException {
    public ResourceNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
