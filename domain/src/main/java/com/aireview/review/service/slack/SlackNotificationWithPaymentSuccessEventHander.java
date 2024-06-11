package com.aireview.review.service.slack;

import com.aireview.review.domains.subscription.PaymentSuccessEvent;
import com.aireview.review.service.slack.SlackMessage;
import com.aireview.review.service.slack.SlackNotificationFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SlackNotificationWithPaymentSuccessEventHander {
    @Value("${server.environment}")
    private String environment;

    private static final String messageFormat =
            "[%s] 환경%n" +
                    "구독 및 카카오 결제 완료%n" +
                    "사용자ID: %s%n" +
                    "%s 회차%n" +
                    "발생시각: %s";
    private final SlackNotificationFeign slackNotificationFeign;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createOrUpdateSubscription(PaymentSuccessEvent paymentSuccessEvent) {
        slackNotificationFeign.sendMessage(new SlackMessage(
                String.format(messageFormat,
                        environment,
                        paymentSuccessEvent.getUserId(),
                        paymentSuccessEvent.getPayment().getSubscriptionId(),
                        paymentSuccessEvent.getPayment().getTimestamp())));

    }
}
