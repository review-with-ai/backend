package com.aireview.review.domains.user.exception;

import com.aireview.review.common.HttpStatus;
import com.aireview.review.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {

    EMAIL_DUPLICATE(HttpStatus.BAD_REQUEST, "USER_400_1", "이미 등록된 이메일입니다."),
    NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_400_2", "존재하지 않거나 차단된 유저입니다.");

    private int status;
    private String code;
    private String reason;
}
