package com.aireview.review.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class UserNameValidator implements ConstraintValidator<UserName, String> {
    private static final Pattern pattern = Pattern.compile("^[가-힣]{3,20}$");
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return pattern.matcher(value).matches();
    }
}
