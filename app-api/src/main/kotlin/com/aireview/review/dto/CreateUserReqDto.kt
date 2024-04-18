package com.aireview.review.dto

import com.aireview.review.domain.user.User

data class CreateUserReqDto(
    val name: String,
    val email: String,
    val nickname: String
) {
    fun toEntity(): User {
        return User.builder()
            .name(name)
            .email(email)
            .nickname(nickname)
            .build();
    }
}
