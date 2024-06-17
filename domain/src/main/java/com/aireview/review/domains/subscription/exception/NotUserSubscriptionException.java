package com.aireview.review.domains.subscription.exception;

import com.aireview.review.common.exception.ErrorCode;
import com.aireview.review.common.exception.ValidationException;

public class NotUserSubscriptionException extends ValidationException {
    private static final ErrorCode errorCode = SubscriptionErrorCode.NOT_USER_SUBSCRIPTION;
    public NotUserSubscriptionException( String message) {
        super(errorCode, message);
    }
}
