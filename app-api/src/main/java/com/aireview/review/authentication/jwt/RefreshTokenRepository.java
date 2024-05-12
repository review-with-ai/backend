package com.aireview.review.authentication.jwt;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@Transactional
public class RefreshTokenRepository {

    private final int expirySeconds;

    private final ValueOperations<String, String> operations;

    private static final String NAME_SPACE = "refreshToken";

    public RefreshTokenRepository(JwtConfig jwtConfig, RedisTemplate redisTemplate) {
        this.expirySeconds = jwtConfig.getRefreshTokenExpirySeconds();
        this.operations = redisTemplate.opsForValue();
    }

    private String getKey(Long id) {
        return String.format("%s:%d", NAME_SPACE, id);
    }

    public void save(final Long userId, final String refreshToken) {
        operations.set(getKey(userId), refreshToken, expirySeconds, TimeUnit.SECONDS);
    }

    public Optional<String> findById(final Long userId) {
        return Optional.ofNullable(operations.get(getKey(userId)));
    }

    public void delete(final Long userId) {
        operations.getAndDelete(getKey(userId));
    }
}
