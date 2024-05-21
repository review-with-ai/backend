package com.aireview.review.exception;

public class RefreshTokenExpiredException extends RefreshTokenException {
    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}
