package com.aireview.review.domains.subscription;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PaymentSuccessEvent extends ApplicationEvent {
    private final Long userId;
    private final Payment payment;

    public PaymentSuccessEvent(Object source, Long userId, Payment payment) {
        super(source);
        this.userId = userId;
        this.payment = payment;
    }
}
