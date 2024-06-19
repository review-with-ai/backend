package com.aireview.review.controller;

import com.aireview.review.common.Authenticated;
import com.aireview.review.config.swagger.ApiExceptionExample;
import com.aireview.review.config.swagger.docs.SubscriptionCancelPossibleExceptions;
import com.aireview.review.config.swagger.docs.SubscriptionPaymentApprovePossibleExceptions;
import com.aireview.review.config.swagger.docs.SubscriptionPaymentReadyPossibleExceptions;
import com.aireview.review.service.SubscriptionPaymentService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
@Tag(name = "구독", description = "구독권(및 결제) 관련 API")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionController {
    private final SubscriptionPaymentService subscriptionPaymentService;

    @GetMapping("/payment")
    @Operation(summary = "구독권 결제 요청", description = "카카오페이 결제 페이지 요청")
    @ApiExceptionExample(SubscriptionPaymentReadyPossibleExceptions.class)
    public ResponseEntity<Void> redirectToKakaoPayment(
            @AuthenticationPrincipal Authenticated authenticated
    ) {
        String redirectUrl = subscriptionPaymentService.ready(authenticated.getUserId());
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirectUrl)
                .build();
    }

    @GetMapping("/payment/approve")
    @Operation(summary = "구독권 결제 승인 요청", description = "카카오페이 결제 후 자동으로 리다이렉트(클라이언트 직접 호출 X)")
    @ApiExceptionExample(SubscriptionPaymentApprovePossibleExceptions.class)
    public void approveKakaoPayment(
            @RequestParam(value = "order_id") String orderId,
            @RequestParam(value = "pg_token") String pgToken
    ) {
        subscriptionPaymentService.approvePayment(orderId, pgToken);
    }

    @GetMapping("/payment/fail")
    @Hidden
    public void failKakaoPayment(
            @RequestParam String tempOrderId
    ) {
        subscriptionPaymentService.deleteSavedPayRequest(tempOrderId);
    }

    @GetMapping("/payment/cancel")
    @Hidden
    public void cancelKakaoPayment(
            @RequestParam String tempOrderId
    ) {
        subscriptionPaymentService.deleteSavedPayRequest(tempOrderId);
    }

    @PostMapping("/renew")
    @Hidden
    public void renewSubscription(
            @AuthenticationPrincipal Authenticated authenticated
    ) {
        subscriptionPaymentService.recurringPay(authenticated.getUserId());
    }

    @PostMapping("/inactive")
    @Operation(summary = "구독권 취소", description = "현재 구독 주기 끝에 구독권 만료")
    @ApiExceptionExample(SubscriptionCancelPossibleExceptions.class)
    public void inactive(
            @AuthenticationPrincipal Authenticated authenticated
    ) {
        subscriptionPaymentService.inactive(authenticated.getUserId());
    }

}
