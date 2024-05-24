package com.aireview.review.config;

import com.aireview.review.domain.user.Email;
import org.springframework.core.convert.converter.Converter;

public class StringToEmailConverter implements Converter<String, Email> {
    @Override
    public Email convert(String source) {
        return new Email(source);
    }
}
