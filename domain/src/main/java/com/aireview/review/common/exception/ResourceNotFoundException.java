package com.aireview.review.common.exception;

public abstract class ResourceNotFoundException extends AiReviewException {
    // TODO: 5/25/24 에러 원인이 된 resource 메시지에 담을 수 없을까
    protected ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
