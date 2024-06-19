package com.aireview.review.config.swagger.docs;

import com.aireview.review.config.swagger.ExplainError;
import com.aireview.review.domains.subscription.exception.AlreadySubscribedException;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionPaymentReadyPossibleExceptions implements PossibleExceptions{

    @ExplainError("사용자가 이미 구독중일때")
    public AlreadySubscribedException 이미_구독중 = AlreadySubscribedException.INSTANCE;
}
