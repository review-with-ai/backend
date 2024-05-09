package com.aireview.review.login.oauth;


public class OAuth2UserInfo {

    private String name;

    private String nickname;

    private String email;

    private String oauthUserId;

    private String oauthProvider;

    public OAuth2UserInfo(String name, String nickname, String email, String oauthUserId, String oauthProvider) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.oauthUserId = oauthUserId;
        this.oauthProvider = oauthProvider;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getOauthUserId() {
        return oauthUserId;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }
}
