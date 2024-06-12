package com.aireview.review.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;

public abstract class RedisRepository<T> {
    private final ValueOperations<String, Object> ops;
    private final ObjectMapper objectMapper;
    private final Class<T> type;

    public RedisRepository(RedisTemplate<String, Object> redisTemplate, Class<T> type) {
        ops = redisTemplate.opsForValue();
        objectMapper = createObjectMapper();
        this.type = type;
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public void save(String key, T value) {
        try {
            ops.set(key, objectMapper.writeValueAsString(value));
        } catch (JsonProcessingException exception) {
            throw new RuntimeException();
        }
    }

    public Optional<T> getAndDelete(String key) {
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
