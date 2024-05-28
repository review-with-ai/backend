package com.aireview.review.domains.user.exception;

import com.aireview.review.common.exception.ValidationException;

public class DuplicateEmailException extends ValidationException {
    public static final DuplicateEmailException INSTANCE = new DuplicateEmailException(UserErrorCode.EMAIL_DUPLICATE);

    private DuplicateEmailException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
