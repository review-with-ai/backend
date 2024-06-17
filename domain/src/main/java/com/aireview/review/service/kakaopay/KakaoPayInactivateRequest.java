package com.aireview.review.service.kakaopay;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoPayInactivateRequest {
    private String cid;
    private String sid;

    public KakaoPayInactivateRequest(String cid, String sid) {
        this.cid = cid;
        this.sid = sid;
    }
}
