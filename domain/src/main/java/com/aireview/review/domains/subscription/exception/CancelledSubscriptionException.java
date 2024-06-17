package com.aireview.review.domains.subscription.exception;

import com.aireview.review.common.exception.ErrorCode;
import com.aireview.review.common.exception.ValidationException;

public class CancelledSubscriptionException extends ValidationException {
    public static final CancelledSubscriptionException INSTANCE =
            new CancelledSubscriptionException(SubscriptionErrorCode.CANCELLED_SUBSCRIPTION);

    private CancelledSubscriptionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
