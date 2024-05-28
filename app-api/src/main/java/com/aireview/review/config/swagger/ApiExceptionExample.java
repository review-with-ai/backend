package com.aireview.review.config.swagger;

import com.aireview.review.config.swagger.docs.PossibleExceptions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiExceptionExample {
    Class<? extends PossibleExceptions> value();
}
