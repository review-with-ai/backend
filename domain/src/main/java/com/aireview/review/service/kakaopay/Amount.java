package com.aireview.review.service.kakaopay;

import lombok.Getter;

@Getter
public class Amount {
    private int total;
    private int tax;

    public Amount(int total, int tax) {
        this.total = total;
        this.tax = tax;
    }
}
