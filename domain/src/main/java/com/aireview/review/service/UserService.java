package com.aireview.review.service;

import com.aireview.review.domain.user.Email;
import com.aireview.review.domain.user.OAuthProvider;
import com.aireview.review.domain.user.User;
import com.aireview.review.domain.user.UserRepository;
import com.aireview.review.exception.validation.DuplicateEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Optional<User> findUser(Long id) {
        return userRepository.findById(id);
    }

    public User join(Email email, String password, String name, String nickname) {
        Assert.notNull(email, "email must be provded");
        Assert.hasLength(password, "encoded password must be provded");

        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new DuplicateEmailException();
                });

        User user = User.of(name, nickname, email, passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findOauthUser(OAuthProvider oauthProvider, String oauthUserId) {
        return userRepository.findByOauthProviderAndOauthUserId(oauthProvider, oauthUserId);
    }

    public User oauthJoin(String name, String nickname, Email email, OAuthProvider oauthProvider, String oAuthUserId) {
        User user = User.oauthUserOf(name, nickname, email, oauthProvider, oAuthUserId);
        return userRepository.save(user);
    }

}
