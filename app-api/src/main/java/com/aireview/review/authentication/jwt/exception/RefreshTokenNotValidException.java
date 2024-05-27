package com.aireview.review.exception;

public class RefreshTokenNotValidException extends RefreshTokenException {
    public static RefreshTokenNotValidException INSTANCE = new RefreshTokenNotValidException(RefreshTokenErrorCode.TOKEN_NOT_VALID);

    private RefreshTokenNotValidException(RefreshTokenErrorCode errorCode) {
        super(errorCode);
    }
}
