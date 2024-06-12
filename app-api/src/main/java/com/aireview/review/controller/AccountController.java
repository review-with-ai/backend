package com.aireview.review.controller;

import com.aireview.review.authentication.jwt.Jwt;
import com.aireview.review.authentication.jwt.JwtService;
import com.aireview.review.config.swagger.ApiExceptionExample;
import com.aireview.review.config.swagger.docs.RefreshTokenPossibleException;
import com.aireview.review.config.swagger.docs.UserJoinPossibleExceptions;
import com.aireview.review.domains.user.domain.User;
import com.aireview.review.model.LoginResponse;
import com.aireview.review.model.JoinRequest;
import com.aireview.review.model.RefreshTokenRequest;
import com.aireview.review.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Account", description = "계정 관련 API")
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping
    @Operation(summary = "일반 회원가입 API", description = "email/pw를 사용한 회원가입")
    @ApiResponse(responseCode = "201", description = "가입 완료")
    @ApiExceptionExample(UserJoinPossibleExceptions.class)
    public ResponseEntity<Void> join(@Valid @RequestBody JoinRequest request) {
        User user = userService.join(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getNickname());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "리프레시 토큰 갱신 API", description = "리프레시 토큰으로 새로운 액세스 토큰 및 리프레시 토큰 발급(로그인과 같은 역할)")
    @ApiExceptionExample(RefreshTokenPossibleException.class)
    public LoginResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) throws BadRequestException {
        Jwt jwt = jwtService.refreshToken(refreshTokenRequest.getUserId(), refreshTokenRequest.getRefreshToken());
        return new LoginResponse(jwt.getAccessToken(), jwt.getRefreshToken(), refreshTokenRequest.getUserId());
    }
}
