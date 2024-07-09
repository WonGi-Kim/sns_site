package com.sparta.easyspring.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Object> redisBlackListTemplate;

    public void set(String key, Object object, int expire) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(object.getClass()));
        redisTemplate.opsForValue().set(key, object, expire, TimeUnit.MINUTES);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void setBlackList(String key, Object object, long milliSeconds) {
        System.out.println("Setting blacklist key with expire time: " + milliSeconds + " ms");
        if (milliSeconds <= 0) {
            throw new IllegalArgumentException("Expire time must be greater than 0");
        }
        redisBlackListTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(object.getClass()));
        redisBlackListTemplate.opsForValue().set(key, object, milliSeconds, TimeUnit.MILLISECONDS);
    }

    public Object getBlackList(String key) {
        return redisBlackListTemplate.opsForValue().get(key);
    }

    public boolean deleteBlackList(String key) {
        return Boolean.TRUE.equals(redisBlackListTemplate.delete(key));
    }

    public boolean hasKeyBlackList(String key) {
        return Boolean.TRUE.equals(redisBlackListTemplate.hasKey(key));
    }

    public void deleteAll() {
        redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys("*")));
    }
}
