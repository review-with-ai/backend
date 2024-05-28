package com.aireview.review.login.oauth;

import com.aireview.review.domains.user.domain.OAuthProvider;

import java.util.Map;

public enum OAuth2UserAttribute2OAuthUserInfoConverter {
    NAVER {
        public OAuth2UserInfo from(Map<String, Object> attributes) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return new OAuth2UserInfo(
                    (String) response.get("name"),
                    (String) response.get("nickname"),
                    (String) response.get("email"),
                    (String) response.get("id"),
                    OAuthProvider.NAVER);

        }
    },

    KAKAO {
        @Override
        protected OAuth2UserInfo from(Map<String, Object> attributes) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            return new OAuth2UserInfo(
                    (String) kakaoAccount.get("name"),
                    (String) profile.get("nickname"),
                    (String) kakaoAccount.get("email"),
                    attributes.get("id").toString(),
                    OAuthProvider.KAKAO);
        }
    };

    protected abstract OAuth2UserInfo from(Map<String, Object> attributes);


    public static OAuth2UserInfo getUserInfoFromOAuth2UserAttribute(String oauthProvider, Map<String, Object> attributes) {
        return OAuth2UserAttribute2OAuthUserInfoConverter.valueOf(oauthProvider.toUpperCase())
                .from(attributes);
    }
}
