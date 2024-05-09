package com.aireview.review.login.oauth;

import org.springframework.util.Assert;

import java.util.Map;

public enum OAuth2UserAttributeConverter {
    NAVER("naver") {
        @Override
        public OAuth2UserInfo from(Map<String, Object> attributes) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            String name = (String) response.getOrDefault("name", null);
            Assert.notNull(name, "oauth user register failed - name null ");

            String nickname = (String) response.getOrDefault("nickname", null);
            Assert.notNull(nickname, "oauth user register failed - nickname null ");

            String email = (String) response.getOrDefault("email", null);
            Assert.notNull(email, "oauth user register failed - email null ");

            String oauthUserId = (String) response.getOrDefault("id", null);
            Assert.notNull(oauthUserId, "oauth user register failed - id null ");

            return new OAuth2UserInfo(name, nickname, email, oauthUserId, oauthProvider);
        }
    };
    // TODO: 5/9/24 KAKAO 추가 필요
    String oauthProvider;

    protected abstract OAuth2UserInfo from(Map<String, Object> attributes);

    OAuth2UserAttributeConverter(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public static OAuth2UserInfo getUserInfoFromOAuth2UserAttribute(String registrationId, Map<String, Object> oauthAttributes) {
        OAuth2UserAttributeConverter converter = OAuth2UserAttributeConverter.valueOf(registrationId.toUpperCase());
        return converter.from(oauthAttributes);
    }


}
