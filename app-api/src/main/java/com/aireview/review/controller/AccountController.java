package com.aireview.review.controller;

import com.aireview.review.authentication.jwt.Jwt;
import com.aireview.review.authentication.jwt.JwtService;
import com.aireview.review.domain.user.User;
import com.aireview.review.login.LoginResponse;
import com.aireview.review.model.JoinRequest;
import com.aireview.review.model.RefreshTokenRequest;
import com.aireview.review.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<Void> join(@Valid @RequestBody JoinRequest request) {
        User user = userService.join(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getNickname());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/refresh-token")
    public LoginResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) throws BadRequestException {
        Jwt jwt = jwtService.refreshToken(refreshTokenRequest.getUserId(), refreshTokenRequest.getRefreshToken());
        return new LoginResponse(jwt.getAccessToken(), jwt.getRefreshToken(), refreshTokenRequest.getUserId());
    }
}
