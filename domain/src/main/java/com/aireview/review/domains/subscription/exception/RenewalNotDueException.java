package com.aireview.review.domains.subscription.exception;

import com.aireview.review.common.exception.ValidationException;

public class RenewalNotDueException extends ValidationException {
    private static final SubscriptionErrorCode ERROR_CODE = SubscriptionErrorCode.RENEWAL_NOT_DUE;

    public RenewalNotDueException(String message) {
        super(ERROR_CODE, message);
    }
}
