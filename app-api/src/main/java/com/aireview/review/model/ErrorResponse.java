package com.aireview.review.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Map;

@Getter
public class ErrorResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> fieldError;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(Map<String, String> fieldError) {
        this.fieldError = fieldError;
    }
}
