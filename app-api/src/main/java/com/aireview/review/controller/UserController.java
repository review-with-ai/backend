package com.aireview.review.controller;

import com.aireview.review.common.exception.ResourceNotFoundException;
import com.aireview.review.domains.user.domain.User;
import com.aireview.review.domains.user.exception.UserErrorCode;
import com.aireview.review.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Hidden
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public User findUser(@PathVariable Long userId) {
        return userService.findUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException(UserErrorCode.NOT_FOUND, userId.toString()));
    }
}
