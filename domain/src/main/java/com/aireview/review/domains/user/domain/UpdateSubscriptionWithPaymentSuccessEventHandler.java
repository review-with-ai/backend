package com.aireview.review.domains.user.domain;


import com.aireview.review.domains.subscription.PaymentSuccessEvent;
import com.aireview.review.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UpdateSubscriptionWithPaymentSuccessEventHandler {
    private final UserService userService;

    // TODO: 6/15/24 user 등록하는데 실패하여도 재시도할 수 있도록 
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateUserSubscriptionStatus(PaymentSuccessEvent paymentSuccessEvent) {
        userService.updateSubscriptionStatus(paymentSuccessEvent.getPayment().getCreatedBy(), true);
    }
}
