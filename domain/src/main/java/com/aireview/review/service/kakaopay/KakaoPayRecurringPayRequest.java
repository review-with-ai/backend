package com.aireview.review.service.kakaopay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPayRecurringPayRequest {
    private String cid;
    private String sid;
    private String partnerOrderId;
    private String partnerUserId;
    private String itemName;
    private int quantity;
    private int totalAmount;
    private int taxFreeAmount;

    public KakaoPayRecurringPayRequest(String cid, String sid, String partnerOrderId, String partnerUserId, String itemName, int totalAmount) {
        this.cid = cid;
        this.sid = sid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.itemName = itemName;
        this.quantity = 1;
        this.totalAmount = totalAmount;
        this.taxFreeAmount = 0;
    }
}
