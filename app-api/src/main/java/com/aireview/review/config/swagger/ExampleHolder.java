package com.aireview.review.config.swagger;

import io.swagger.v3.oas.models.examples.Example;
import lombok.Getter;

@Getter
public class ExampleHolder {
    private Example example;
    private String  name;
    private int code;

    public ExampleHolder(Example example, String name, int code) {
        this.example = example;
        this.name = name;
        this.code = code;
    }
}
