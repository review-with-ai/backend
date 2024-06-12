package com.aireview.review.service.kakaopay;

import com.aireview.review.config.RedisRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class SavedPayRequestRepository extends RedisRepository<SavedPayRequest> {
    public SavedPayRequestRepository(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, SavedPayRequest.class);
    }
}
