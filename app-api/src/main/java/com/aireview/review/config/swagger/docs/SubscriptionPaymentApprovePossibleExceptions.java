package com.aireview.review.config.swagger.docs;

import com.aireview.review.common.exception.AiReviewException;
import com.aireview.review.config.swagger.ExplainError;
import com.aireview.review.domains.subscription.exception.AlreadySubscribedException;
import com.aireview.review.domains.subscription.exception.PayRequestNotFoundException;
import com.aireview.review.domains.subscription.exception.PaymentApprovalFailException;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionPaymentApprovePossibleExceptions implements PossibleExceptions {

    @ExplainError("카카오페이 결제 승인 실패")
    public AiReviewException 카카오_결제_실패 = PaymentApprovalFailException.INSTANCE;

    @ExplainError("결제 준비를 거치지 않은 결제 요청일때")
    public AiReviewException 유효하지_않은_결제_요청 = PayRequestNotFoundException.INSTANCE;

    @ExplainError("사용자가 이미 구독중일때")
    public AlreadySubscribedException 이미_구독중 = AlreadySubscribedException.INSTANCE;

}
