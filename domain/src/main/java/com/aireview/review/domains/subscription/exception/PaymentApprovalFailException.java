package com.aireview.review.domains.subscription.exception;

import com.aireview.review.common.exception.AiReviewException;

public class PaymentApprovalFailException extends AiReviewException {
    public static final PaymentApprovalFailException INSTANCE = new PaymentApprovalFailException(SubscriptionErrorCode.PAY_APPROVE_FAIL);

    private PaymentApprovalFailException(SubscriptionErrorCode errorCode) {
        super(errorCode);
    }
}
