package com.aireview.review.service.kakaopay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPayReadyRequest {
    private String cid;
    private String partnerOrderId;
    private String partnerUserId;
    private String itemName;
    private int quantity;
    private int totalAmount;
    private int taxFreeAmount;
    private String approvalUrl;
    private String cancelUrl;
    private String failUrl;

    public KakaoPayReadyRequest(
            String cid,
            String partnerOrderId,
            String partnerUserId,
            String itemName,
            int quantity,
            int totalAmount,
            int taxFreeAmount,
            String approvalUrl,
            String cancelUrl,
            String failUrl) {
        this.cid = cid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.taxFreeAmount = taxFreeAmount;
        this.approvalUrl = approvalUrl;
        this.cancelUrl = cancelUrl;
        this.failUrl = failUrl;
    }
}
