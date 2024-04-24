package com.aireview.review

import com.aireview.review.domain.user.User
import com.aireview.review.dto.CreateUserReqDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class AppApiApplicationTests(
    @LocalServerPort private val port: Int,
    @Autowired private val restTemplate: TestRestTemplate
) {

    @Test
    fun contextLoads() {
    }

    @Test
    fun 유저를_등록한다() {
        val userId = restTemplate.postForObject(
            "http://localhost:$port/v1/users",
            CreateUserReqDto("홍길동", "hong@gmail.com", "hongs"),
            Long::class.java
        )
        val user = restTemplate.getForObject(
            "http://localhost:$port/v1/users/$userId",
            User::class.java
        )
        assertThat(user).isNotNull
        assertThat(user!!.id).isNotNull()
        assertThat(user.name).isEqualTo("홍길동")
    }


}
