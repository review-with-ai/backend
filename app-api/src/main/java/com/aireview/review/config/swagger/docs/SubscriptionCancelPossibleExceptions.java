package com.aireview.review.config.swagger.docs;

import com.aireview.review.common.exception.AiReviewException;
import com.aireview.review.common.exception.ResourceNotFoundException;
import com.aireview.review.config.swagger.ExplainError;
import com.aireview.review.domains.subscription.exception.CancelledSubscriptionException;
import com.aireview.review.domains.subscription.exception.SubscriptionErrorCode;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionCancelPossibleExceptions implements PossibleExceptions {
    @ExplainError("유저가 구독하지 않은 상태일때")
    public AiReviewException 구독권_없음 = new ResourceNotFoundException(SubscriptionErrorCode.SUBSCRIPTION_NOT_FOUND, "by userId 1");

    @ExplainError("이미 취소된 구독권일때")
    public AiReviewException 이미_취소된_구독권 = CancelledSubscriptionException.INSTANCE;
}
