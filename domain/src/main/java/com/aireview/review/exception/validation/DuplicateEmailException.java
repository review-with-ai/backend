package com.aireview.review.exception.validation;

public class DuplicateEmailException extends ValidationException {
    private static String DEAFULT_MESSAGE = "중복된 이메일입니다.";

    public DuplicateEmailException() {
        super(DEAFULT_MESSAGE);
    }
}
