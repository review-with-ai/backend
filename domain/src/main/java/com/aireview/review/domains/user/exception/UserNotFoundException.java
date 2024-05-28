package com.aireview.review.domains.user.exception;

import com.aireview.review.common.exception.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public static final UserNotFoundException INSTANCE = new UserNotFoundException(UserErrorCode.NOT_FOUND);

    private UserNotFoundException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
