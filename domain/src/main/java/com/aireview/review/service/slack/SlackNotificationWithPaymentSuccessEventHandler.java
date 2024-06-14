package com.aireview.review.service.slack;

import com.aireview.review.domains.subscription.PaymentSuccessEvent;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SlackNotificationWithPaymentSuccessEventHandler {
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
    public void sendPaymentSuccessMessage(PaymentSuccessEvent paymentSuccessEvent) {
        try {
            slackNotificationFeign.sendMessage(new SlackMessage(
                    String.format(messageFormat,
                            environment,
                            paymentSuccessEvent.getUserId(),
                            paymentSuccessEvent.getPayment().getSeq(),
                            paymentSuccessEvent.getPayment().getTimestamp())));

        } catch (Exception exception) {
            Sentry.captureException(exception);
        }
    }
}
