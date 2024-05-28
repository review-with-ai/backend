package com.aireview.review.config.swagger;

import io.swagger.v3.oas.models.examples.Example;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExampleHolder {
    private Example example;
    private String  name;
    private int code;
}
