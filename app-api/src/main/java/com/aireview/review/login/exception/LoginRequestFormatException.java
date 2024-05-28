package com.aireview.review.login.exception;


import org.springframework.security.core.AuthenticationException;

public class LoginRequestFormatException extends AuthenticationException {
    public static final LoginRequestFormatException INSTANCE = new LoginRequestFormatException();

    private LoginRequestFormatException() {
        super("login request read fail");
    }
}
