package com.aireview.review.login;

public enum Role {
    USER("ROLE_USER"),
    OAUTH_USER("ROLE_OAUTH_USER");
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
