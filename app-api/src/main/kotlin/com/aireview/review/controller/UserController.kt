package com.aireview.review.controller

import com.aireview.review.dto.CreateUserReqDto
import com.aireview.review.service.UserService
import com.aireview.review.domain.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserController(
    @Autowired val userService: UserService
) {

    @PostMapping
    fun createUser(@RequestBody request: CreateUserReqDto): Long {
        return userService.join(request)
    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: Long): User {
        return userService.get(userId)
    }

}