package com.aireview.review.service.kakaopay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPayInactivateResponse {
    private String cid;
    private String sid;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime lastApprovedAt;
    private LocalDateTime inactivatedAt;

    public KakaoPayInactivateResponse(String cid, String sid, String status, LocalDateTime createdAt, LocalDateTime lastApprovedAt, LocalDateTime inactivatedAt) {
        this.cid = cid;
        this.sid = sid;
        this.status = status;
        this.createdAt = createdAt;
        this.lastApprovedAt = lastApprovedAt;
        this.inactivatedAt = inactivatedAt;
    }
}
