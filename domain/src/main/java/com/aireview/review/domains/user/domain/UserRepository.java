package com.aireview.review.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(Email email);

    Optional<User> findByOauthProviderAndOauthUserId(OAuthProvider oauthProvider, String oauthUserId);
}
