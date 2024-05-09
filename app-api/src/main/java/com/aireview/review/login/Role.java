package com.aireview.review.model;

public enum Role {
    USER("ROLE_USER");
    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String value(){
        return value;
    }
    public static Role of(String name){
        return Role.valueOf(name);
    }
}
