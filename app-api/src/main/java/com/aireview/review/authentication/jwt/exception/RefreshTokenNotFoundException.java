package com.aireview.review.authentication.jwt.exception;


public class RefreshTokenNotFoundException extends RefreshTokenException {
    public static final RefreshTokenNotFoundException INSTANCE = new RefreshTokenNotFoundException(RefreshTokenErrorCode.TOKEN_NOT_FOUND);

    private RefreshTokenNotFoundException(RefreshTokenErrorCode errorCode) {
        super(errorCode);
    }
}
