package com.aireview.review.exception;

import com.aireview.review.common.exception.AiReviewException;

public class RefreshTokenException extends AiReviewException {
    public RefreshTokenException(RefreshTokenErrorCode errorCode) {
        super(errorCode);
    }
}
