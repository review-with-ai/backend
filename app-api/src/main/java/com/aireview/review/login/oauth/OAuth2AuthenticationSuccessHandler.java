package com.aireview.review.login.oauth;

import com.aireview.review.authentication.jwt.JwtService;
import com.aireview.review.domain.user.User;
import com.aireview.review.login.LoginSuccessHandler;
import com.aireview.review.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler extends LoginSuccessHandler {
    private final UserService userService;

    public OAuth2AuthenticationSuccessHandler(
            JwtService jwtService,
            ObjectMapper objectMapper,
            UserService userService) {
        super(jwtService, objectMapper);
        this.userService = userService;
    }

    @Override
    protected User processAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        OAuth2AuthenticationToken oauthAuthentication = (OAuth2AuthenticationToken) authentication;
        String oauthProvider = oauthAuthentication.getAuthorizedClientRegistrationId();
        OAuth2User oauth2User = oauthAuthentication.getPrincipal();

        OAuth2UserInfo userInfo = getOAuth2UserInfo(oauthProvider, oauth2User);

        Optional<User> oauthUser = userService.findOauthUser(userInfo.getOauthProvider(), userInfo.getOauthUserId());
        return oauthUser
                .orElseGet(() -> persistUser(userInfo));
    }


    private OAuth2UserInfo getOAuth2UserInfo(String oauthProvider, OAuth2User oauth2User) {
        return OAuth2UserAttribute2OAuthUserInfoConverter
                .getUserInfoFromOAuth2UserAttribute(oauthProvider, oauth2User.getAttributes());
    }

    private User persistUser(OAuth2UserInfo userInfo) {
        return userService.oauthJoin(
                userInfo.getName(),
                userInfo.getNickname(),
                userInfo.getEmail(),
                userInfo.getOauthProvider(),
                userInfo.getOauthUserId()
        );
    }
}
