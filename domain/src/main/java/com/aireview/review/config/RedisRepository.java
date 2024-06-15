package com.aireview.review.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RedisRepository {
    private final ValueOperations<String, Object> ops;
    private final ObjectMapper objectMapper;


    public RedisRepository(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.ops = redisTemplate.opsForValue();
        this.objectMapper = objectMapper;
    }

    public void save(String key, Object value) {
        try {
            ops.set(key, objectMapper.writeValueAsString(value));
        } catch (JsonProcessingException exception) {
            throw new RuntimeException();
        }
    }

    public <T> Optional<T> getAndDelete(String key, Class<T> type) {
        String json = (String) ops.getAndDelete(key);
        if (StringUtils.isBlank(json)) {
            return Optional.empty();
        } else {
            try {
                return Optional.of(objectMapper.readValue(json, type));
            } catch (JsonProcessingException exception) {
                throw new RuntimeException();
            }
        }
    }
}
