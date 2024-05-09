package com.aireview.review.login.oauth;

import com.aireview.review.authentication.jwt.Jwt;
import com.aireview.review.domain.user.User;
import com.aireview.review.login.LoginResponse;
import com.aireview.review.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final Jwt jwt;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthAuthentication = (OAuth2AuthenticationToken) authentication;
        String oauthProvider = oauthAuthentication.getAuthorizedClientRegistrationId();
        OAuth2User oauth2User = oauthAuthentication.getPrincipal();

        // TODO: 5/9/24 유저 저장시 createdAt, updatedAt null 로 update 안됨. auditing listener 동작하지 않음.
        Optional<User> oauthUser = userService.findOauthUser(oauthProvider, oauth2User.getName());
        User user = oauthUser
                .orElseGet(() -> persistUser(oauthProvider, oauth2User));


        String token = jwt.createJwt(user.getId(), user.getEmail(), oauth2User.getAuthorities());
        response.setContentType(MediaType.APPLICATION_JSON.getType());
        response.getWriter().write(
                objectMapper.writeValueAsString(new LoginResponse(token, user.getId()))
        );
        response.getWriter().flush();
        response.getWriter().close();
    }

    public User persistUser(String oauthProvider, OAuth2User oauth2User) {
        OAuth2UserInfo userInfo = OAuth2UserAttributeConverter
                .getUserInfoFromOAuth2UserAttribute(oauthProvider, oauth2User.getAttributes());

        return userService.oauthJoin(
                userInfo.getName(),
                userInfo.getNickname(),
                userInfo.getEmail(),
                userInfo.getOauthProvider(),
                userInfo.getOauthUserId()
        );
        // TODO: 5/9/24  oauth user 정보가 없으면? IllegalArgumentException 어떻게 처리?
        // TODO: 5/9/24  user 정보를 가져오다가 예외가 발생하면 어떻게 처리할 것인가? DataAccessException 어떻게 처리?
    }
}
