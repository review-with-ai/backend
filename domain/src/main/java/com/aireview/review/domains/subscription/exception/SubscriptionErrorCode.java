package com.aireview.review.domains.subscription.exception;

import com.aireview.review.common.HttpStatus;
import com.aireview.review.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum SubscriptionErrorCode implements ErrorCode {

    ALREADY_SUBSCRIBED(HttpStatus.BAD_REQUEST, "SUBSCRIPTION_400_1", "이미 구독한 사용자입니다."),
    PAY_REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST, "SUBSCRIPTION_400_2", "존재하지 않는 결제 요청입니다."),
    SUBSCRIPTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "SUBSCRIPTION_400_3", "구독권이 존재하지 않습니다."),
    RENEWAL_NOT_DUE(HttpStatus.BAD_REQUEST, "SUBSCRIPTION_400_4", "구독 갱신 주기가 아닙니다."),
    NOT_USER_SUBSCRIPTION(HttpStatus.BAD_REQUEST, "SUBSCRIPTION_400_5", "사용자의 구독권이 아닙니다."),
    PAY_APPROVE_FAIL(HttpStatus.INTERNAL_SERVER, "SUBSCRIPTION_500_1", "결제 승인에 실패하였습니다.");

    private int status;
    private String code;
    private String reason;

    SubscriptionErrorCode(int status, String code, String reason) {
        this.status = status;
        this.code = code;
        this.reason = reason;
    }
}
