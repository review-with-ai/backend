package com.aireview.review.login.usernamepassword;

import com.aireview.review.domain.user.User;
import com.aireview.review.domain.user.UserRepository;
import com.aireview.review.exception.ResourceNotFoundException;
import com.aireview.review.login.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) {
        // TODO: 4/30/24 username 중복되는 경우 에러 처리 필요
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with username " + username));
        return new CustomUserDetails(user);
    }
}
