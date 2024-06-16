package com.aireview.review.controller;

import com.aireview.review.authentication.CustomAuthenticatedPrincipal;
import com.aireview.review.service.SubscriptionPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionController {
    private final SubscriptionPaymentService subscriptionPaymentService;

    @GetMapping("/payment")
    public ResponseEntity<Void> redirectToKakaoPayment(
            @AuthenticationPrincipal CustomAuthenticatedPrincipal principal
    ) {
        String redirectUrl = subscriptionPaymentService.ready(principal.id());
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirectUrl)
                .build();
    }

    @GetMapping("/payment/approve")
    public void approveKakaoPayment(
            @RequestParam(value = "order_id") String orderId,
            @RequestParam(value = "pg_token") String pgToken
    ) {
        subscriptionPaymentService.approvePayment(orderId, pgToken);
    }

    @GetMapping("/payment/fail")
    public void failKakaoPayment(
            @RequestParam String tempOrderId
    ) {
        subscriptionPaymentService.deleteSavedPayRequest(tempOrderId);
    }

    @GetMapping("/payment/cancel")
    public void cancelKakaoPayment(
            @RequestParam String tempOrderId
    ) {
        subscriptionPaymentService.deleteSavedPayRequest(tempOrderId);
    }

    @GetMapping("/renew")
    public void renewSubscription(
            @AuthenticationPrincipal CustomAuthenticatedPrincipal authenticatedPrincipal,
            @RequestParam(value = "subscriptionId") Long subscriptionId
    ) {
        subscriptionPaymentService.recurringPay(authenticatedPrincipal.id(), subscriptionId);
    }

    @GetMapping("/inactive")
    public void inactive(
            @AuthenticationPrincipal CustomAuthenticatedPrincipal authenticatedPrincipal
            ) {
        subscriptionPaymentService.inactive(authenticatedPrincipal.id());
    }

}
