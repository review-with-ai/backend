package com.aireview.review.service;

import com.aireview.review.common.exception.ResourceNotFoundException;
import com.aireview.review.domains.user.domain.OAuthProvider;
import com.aireview.review.domains.user.domain.User;
import com.aireview.review.domains.user.domain.UserRepository;
import com.aireview.review.domains.user.exception.DuplicateEmailException;
import com.aireview.review.domains.user.exception.UserErrorCode;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;


    public Optional<User> findUser(Long id) {
        return userRepository.findById(id);
    }

    public User join(String email, String password, String name, String nickname) {
        Assert.notNull(email, "email must be provded");
        Assert.hasLength(password, "encoded password must be provded");

        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw DuplicateEmailException.INSTANCE;
                });

        User user = User.of(name, nickname, email, passwordEncoder.encode(password));

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findOauthUser(OAuthProvider oauthProvider, String oauthUserId) {
        return userRepository.findByOauthProviderAndOauthUserId(oauthProvider, oauthUserId);
    }

    public User oauthJoin(String name, String nickname, String email, OAuthProvider oauthProvider, String oAuthUserId) {
        User user = User.oauthUserOf(name, nickname, email, oauthProvider, oAuthUserId);

        return userRepository.save(user);
    }

    public boolean hasSubscribed(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(UserErrorCode.NOT_FOUND, userId.toString()));
        return user.hasSubscribed();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateSubscriptionStatus(Long userId, boolean hasSubscribed) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(UserErrorCode.NOT_FOUND, userId.toString()));
        user.updateSubscriptionStatus(hasSubscribed);
    }
}
