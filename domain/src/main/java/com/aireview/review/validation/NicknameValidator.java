package com.aireview.review.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NicknameValidator implements ConstraintValidator<Nickname, String> {
    private static final Pattern pattern = Pattern.compile("^[a-zA-Z0-9가-힣!@#$%^&*()_+={}\\[\\]:;\"'<>,.?\\/~`-]{2,20}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return pattern.matcher(value).matches();
    }
}
