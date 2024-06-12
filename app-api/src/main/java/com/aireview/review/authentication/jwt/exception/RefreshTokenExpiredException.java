package com.aireview.review.authentication.jwt.exception;

public class RefreshTokenExpiredException extends RefreshTokenException {
    public static RefreshTokenExpiredException INSTANCE = new RefreshTokenExpiredException(RefreshTokenErrorCode.TOKEN_EXPIRED);
    private RefreshTokenExpiredException(RefreshTokenErrorCode errorCode) {
        super(errorCode);
    }
}
