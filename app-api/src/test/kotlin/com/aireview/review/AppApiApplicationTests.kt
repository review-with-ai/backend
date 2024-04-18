package com.aireview.review

import com.aireview.review.domain.user.User
import com.aireview.review.dto.CreateUserReqDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class AppApiApplicationTests(
    @LocalServerPort private val port: Int,
    @Autowired private val restTemplate: TestRestTemplate
) {

    @Test
    fun contextLoads() {
    }

    @Test
    @Transactional
    fun 유저를_등록한다() {
        val por: Int = 8080
        val userId = restTemplate.postForObject(
            "http://localhost:${port}/v1/users",
            CreateUserReqDto("홍길동", "hong@email.com", "hongs"),
            Long::class.java
        )
        val user = restTemplate.getForObject(
            "http://localhost:${port}/v1/users/{userId}",
            User::class.java,
            userId
        )
        assertThat(user).isNotNull
        assertThat(user!!.id).isNotNull()
        assertThat(user.name).isEqualTo("홍길동")
    }


}
