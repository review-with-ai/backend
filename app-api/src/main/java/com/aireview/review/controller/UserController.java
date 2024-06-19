package com.aireview.review.controller;

import com.aireview.review.common.Authenticated;
import com.aireview.review.common.exception.ResourceNotFoundException;
import com.aireview.review.config.swagger.ApiExceptionExample;
import com.aireview.review.config.swagger.docs.UserJoinPossibleExceptions;
import com.aireview.review.domains.user.domain.User;
import com.aireview.review.domains.user.exception.UserErrorCode;
import com.aireview.review.model.JoinRequest;
import com.aireview.review.model.UserMeResponse;
import com.aireview.review.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "유저", description = "유저 계정 관련 API")
@RequiredArgsConstructor
public class UserController {

    private static final String USER_LOCATION = "/api/v1/users/";

    private final UserService userService;

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
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create(USER_LOCATION + user.getId()))
                .build();
    }


    @GetMapping("/me")
    @Operation(summary = "토큰으로 유저 정보 조회", description = "쿠키에 담긴 jwt 토큰으로 유저 기본 정보(userId등) 조회")
    public UserMeResponse me(@AuthenticationPrincipal Authenticated auth) {
        User user = userService.findUser(auth.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(UserErrorCode.NOT_FOUND, auth.getUserId().toString()));

        return UserMeResponse.from(user);
    }

    @GetMapping("/{userId}")
    @Hidden
    public User getUser(@AuthenticationPrincipal Authenticated auth,
                        @PathVariable Long userId
    ) {
        User user = userService.findUser(auth.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(UserErrorCode.NOT_FOUND, auth.getUserId().toString()));
        return user;
    }
}
