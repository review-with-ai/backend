package com.aireview.review.authentication.jwt.exception;

public class NotEligibleUserException extends RefreshTokenException {
    public static final NotEligibleUserException INSTANCE = new NotEligibleUserException(RefreshTokenErrorCode.NOT_ELIGIBLE_USER);

    private NotEligibleUserException(RefreshTokenErrorCode errorCode) {
        super(errorCode);
    }
}
