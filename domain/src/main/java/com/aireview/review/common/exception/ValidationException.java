package com.aireview.review.exception.validation;

import com.aireview.review.exception.AiReviewException;
import com.aireview.review.exception.ErrorCode;

public class ValidationException extends AiReviewException {
    protected ValidationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
