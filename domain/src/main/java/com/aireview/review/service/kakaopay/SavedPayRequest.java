package com.aireview.review.service.kakaopay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SavedPayRequest {
    private String tid;
    private String tempOrderId;
    private String cid;
    private String tempUserId;
    private int totalAmount;
    private String itemName;
    private LocalDateTime createdAt;
    private byte seq;
    private Long userId;
}
