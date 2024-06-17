package com.aireview.review.service.kakaopay;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
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

    public SavedPayRequest(
            String tid,
            String tempOrderId,
            String cid,
            String tempUserId,
            int totalAmount,
            String itemName,
            LocalDateTime createdAt,
            byte seq,
            Long userId) {
        this.tid = tid;
        this.tempOrderId = tempOrderId;
        this.cid = cid;
        this.tempUserId = tempUserId;
        this.totalAmount = totalAmount;
        this.itemName = itemName;
        this.createdAt = createdAt;
        this.seq = seq;
        this.userId = userId;
    }
}
