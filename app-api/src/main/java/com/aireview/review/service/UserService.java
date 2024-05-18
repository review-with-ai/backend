package com.aireview.review.service;

import com.aireview.review.domain.user.User;
import com.aireview.review.domain.user.UserRepository;
import com.aireview.review.login.usernamepassword.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Optional<User> findUser(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO: 4/30/24 username 중복되는 경우 에러 처리 필요 
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found with username " + username));
        return new CustomUserDetails(user);
    }

    public User join(String email, String password, String name, String nickname) {
        // TODO: 4/29/24 중복 체크 필요 
        User user = new User(name, nickname, email, passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findOauthUser(String oauthProvider, String oauthUserId) {
        return userRepository.findByOauthProviderAndOauthUserId(oauthProvider, oauthUserId);
    }

    public User oauthJoin(String name, String nickname, String email, String oauthProvider, String oAuthUserId) {
        User user = new User(name, nickname, email, oauthProvider, oAuthUserId);
        return userRepository.save(user);
    }

}
