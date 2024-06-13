package com.aireview.review.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RedisRepositoryUtil {
    private static ValueOperations<String, Object> ops;
    private static ObjectMapper objectMapper;

    private RedisTemplate<String, Object> redisTemplate;
    private ObjectMapper objectMapperBean;

    @PostConstruct
    private void init() {
        ops = redisTemplate.opsForValue();
        objectMapper = objectMapperBean;
    }

    public RedisRepositoryUtil(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapperBean) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapperBean;
    }

    public static void save(String key, Object value) {
        try {
            ops.set(key, objectMapper.writeValueAsString(value));
        } catch (JsonProcessingException exception) {
            throw new RuntimeException();
        }
    }

    public static <T> Optional<T> getAndDelete(String key, Class<T> type) {
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
