package com.aireview.review.authentication.jwt.exception;

public class RefreshTokenNotIssuedByApp extends RefreshTokenException {
    public static final RefreshTokenNotIssuedByApp INSTANCE =
            new RefreshTokenNotIssuedByApp(RefreshTokenErrorCode.TOKEN_NOT_ISSUED_BY_APP);

    private RefreshTokenNotIssuedByApp(RefreshTokenErrorCode errorCode) {
        super(errorCode);
    }
}
