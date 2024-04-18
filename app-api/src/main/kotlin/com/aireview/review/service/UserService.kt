package com.aireview.review.service

import com.aireview.review.dto.CreateUserReqDto
import com.aireview.review.domain.user.User
import com.aireview.review.domain.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.RuntimeException

@Service
class UserService(
    @Autowired val userRepository: UserRepository
) {
    fun join(request: CreateUserReqDto): Long {
        return userRepository.save(request.toEntity()).id;
    }

    fun get(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { RuntimeException("$userId not found") };
    }
}