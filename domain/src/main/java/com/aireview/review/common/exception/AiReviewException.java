package com.aireview.review.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AiReviewException extends RuntimeException {
    private ErrorCode errorCode;
}
