package com.aireview.review.config.swagger;


import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExplainError {
    String value() default "";
}
