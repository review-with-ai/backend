package com.aireview.review.domain.user;

import com.aireview.review.domain.user.User;
import com.aireview.review.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void 유저가_등록된다() {
        User user = User.builder()
                .name("홍길동")
                .nickname("hongs")
                .email("hong@gmail.com")
                .build();
        userRepository.save(user);

        Optional<User> persistedUser = userRepository.findById(user.getId());
        assertThat(persistedUser.get())
                .isNotNull()
                .isEqualTo(user);

    }
}
