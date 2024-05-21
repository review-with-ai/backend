package com.aireview.review.login.oauth;

import com.aireview.review.domain.user.User;

import java.util.Map;

public class OAuth2UserAttributeConverter {
    public static class NAVER {
        public OAuth2UserInfo from(Map<String, Object> attributes) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return new OAuth2UserInfo(
                    (String) response.get("name"),
                    (String) response.get("nickname"),
                    (String) response.get("email"),
                    (String) response.get("oauthUserId"),
                    User.OAuthProvider.NAVER);

        }
    },

    KAKAO {
        @Override
        protected OAuth2UserInfo from (Map < String, Object > attributes){
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            return new OAuth2UserInfo(
                    (String) kakaoAccount.get("name"),
                    (String) profile.get("nickname"),
                    (String) kakaoAccount.get("email"),
                    attributes.get("id").toString(),
                    this.name());
        }
    }

    ;

    protected abstract OAuth2UserInfo from(Map<String, Object> attributes);

    public static OAuth2UserInfo getUserInfoFromOAuth2UserAttribute(String registrationId, Map<String, Object> oauthAttributes) {
        OAuth2UserAttributeConverter converter = OAuth2UserAttributeConverter.valueOf(registrationId.toUpperCase());
        return converter.from(oauthAttributes);
    }


}
