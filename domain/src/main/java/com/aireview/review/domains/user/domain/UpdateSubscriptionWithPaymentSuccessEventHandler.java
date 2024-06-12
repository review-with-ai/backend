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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateUserSubscriptionStatus(PaymentSuccessEvent paymentSuccessEvent) {
        userService.updateSubscriptionStatus(paymentSuccessEvent.getUserId(), true);
    }
}
