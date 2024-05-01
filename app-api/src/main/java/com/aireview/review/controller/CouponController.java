package com.aireview.review.controller;

import com.aireview.review.config.security.AuthenticatedPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupon")
@RequiredArgsConstructor
public class CouponController {

    @GetMapping("/fcfs")
    public ResponseEntity<Void> issueCouponByFcfs(@AuthenticationPrincipal AuthenticatedPrincipal authenticatedPrincipal) {
        Long userId = authenticatedPrincipal.getId();
        String email = authenticatedPrincipal.getEmail();
        if (userId == null || email == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }
}
