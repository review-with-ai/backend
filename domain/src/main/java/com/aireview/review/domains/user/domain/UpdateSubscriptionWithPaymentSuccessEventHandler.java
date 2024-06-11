package com.aireview.review.domains.user.domain;


import com.aireview.review.domains.subscription.PaymentSuccessEvent;
import com.aireview.review.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateSubscriptionWithPaymentSuccessEventHandler {
    private final UserService userService;

    @EventListener
    public void updateUserSubscriptionStatus(PaymentSuccessEvent paymentSuccessEvent) {
        userService.updateSubscriptionStatus(paymentSuccessEvent.getUserId(), true);
    }
}
