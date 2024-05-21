package com.aireview.review.service;

import com.aireview.review.domain.user.OAuthProvider;
import com.aireview.review.domain.user.User;
import com.aireview.review.domain.user.UserRepository;
import com.aireview.review.exception.validation.DuplicateEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findUser(Long id) {
        return userRepository.findById(id);
    }

    public User join(String email, String password, String name, String nickname) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new DuplicateEmailException();
                });
        // TODO: 5/22/24 password encode 모듈 정리 필요
        User user = User.of(name, nickname, email, password);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findOauthUser(String oauthProvider, String oauthUserId) {
        return userRepository.findByOauthProviderAndOauthUserId(oauthProvider, oauthUserId);
    }

    public User oauthJoin(String name, String nickname, String email, OAuthProvider oauthProvider, String oAuthUserId) {
        User user = User.oauthUserOf(name, nickname, email, oauthProvider, oAuthUserId);
        return userRepository.save(user);
    }

}
