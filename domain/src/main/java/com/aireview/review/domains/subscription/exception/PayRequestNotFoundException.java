package com.aireview.review.domains.subscription.exception;

import com.aireview.review.common.exception.ValidationException;

public class PayRequestNotFoundException extends ValidationException {
    public static PayRequestNotFoundException INSTANCE = new PayRequestNotFoundException(SubscriptionErrorCode.PAY_REQUEST_NOT_FOUND);

    private PayRequestNotFoundException(SubscriptionErrorCode errorCode){
        super(errorCode);
    }
}
