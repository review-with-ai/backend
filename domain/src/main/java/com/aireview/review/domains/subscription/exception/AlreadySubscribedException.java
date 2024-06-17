package com.aireview.review.domains.subscription.exception;

import com.aireview.review.common.exception.AiReviewException;

public class AlreadySubscribedException extends AiReviewException {
    public static final AlreadySubscribedException INSTANCE = new AlreadySubscribedException(SubscriptionErrorCode.ALREADY_SUBSCRIBED);

    private AlreadySubscribedException(SubscriptionErrorCode errorCode) {
        super(errorCode);
    }
}
