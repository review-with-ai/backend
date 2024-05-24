package com.aireview.review.login.usernamepassword;

import com.aireview.review.domain.user.Email;
import com.aireview.review.domain.user.User;
import com.aireview.review.domain.user.UserRepository;
import com.aireview.review.login.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(Email.of(username))
                .orElseThrow(() -> new UsernameNotFoundException("user not found with username " + username));
        return new UserDetails(user);
    }
}
