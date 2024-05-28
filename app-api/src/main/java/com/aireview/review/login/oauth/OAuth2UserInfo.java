package com.aireview.review.login.oauth;

import com.aireview.review.domains.user.domain.OAuthProvider;
import org.springframework.util.Assert;

public class OAuth2UserInfo {

    private String name;

    private String nickname;

    private String email;

    private String oauthUserId;

    private OAuthProvider oauthProvider;

    public OAuth2UserInfo(String name, String nickname, String email, String oauthUserId, OAuthProvider oAuthProvider) {
        Assert.notNull(name, "oauth user register failed - name null ");
        Assert.notNull(nickname, "oauth user register failed - nickname null ");
        Assert.notNull(email, "oauth user register failed - email null ");
        Assert.notNull(oauthUserId, "oauth user register failed - user id null ");
        Assert.notNull(oAuthProvider, "oauth user register failed - oauth provider null ");

        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.oauthUserId = oauthUserId;
        this.oauthProvider = oAuthProvider;
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

    public OAuthProvider getOauthProvider() {
        return oauthProvider;
    }
}
