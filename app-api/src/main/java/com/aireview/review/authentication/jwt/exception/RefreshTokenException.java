package com.aireview.review.authentication.jwt.exception;

import com.aireview.review.common.exception.AiReviewException;

public abstract class RefreshTokenException extends AiReviewException {
    protected RefreshTokenException(RefreshTokenErrorCode errorCode) {
        super(errorCode);
    }
    protected RefreshTokenException(RefreshTokenErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
