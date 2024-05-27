package com.aireview.review.exception;

public class ResourceNotFoundException extends AiReviewException {
    // TODO: 5/25/24 resource 메시지에 담을 수 없을까
    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
