package com.aireview.review.authentication.jwt.exception;

public class RefreshTokenNotValidException extends RefreshTokenException {
    private static final RefreshTokenErrorCode errorCode = RefreshTokenErrorCode.TOKEN_NOT_VALID;

    public static RefreshTokenNotValidException INSTANCE = new RefreshTokenNotValidException(errorCode);

    private RefreshTokenNotValidException(RefreshTokenErrorCode errorCode) {
        super(errorCode);
    }

    public RefreshTokenNotValidException(String message) {
        super(errorCode, message);
    }
}
