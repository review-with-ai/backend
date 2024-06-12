package com.aireview.review.common.exception;

public interface ErrorCode {

    int getStatus();

    String getCode();

    String getReason();
}
