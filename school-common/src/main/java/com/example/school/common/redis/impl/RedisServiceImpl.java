package com.example.school.common.redis.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.redis.RedisService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/12 11:30
 * description:
 */
@AllArgsConstructor
@Component
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate redisTemplate;

    @Override
    public Optional<JSONObject> get(String key) {
        ValueOperations<String, JSONObject> valueOperations = redisTemplate.opsForValue();
        return Optional.ofNullable(valueOperations.get(key));
    }

    @Override
    public void setContainExpire(String key, JSONObject value) {
        ValueOperations<String, JSONObject> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, 30, TimeUnit.MINUTES);
    }

    @Override
    public void setContainExpire(String key, JSONObject value, long timeout, TimeUnit unit) {
        ValueOperations<String, JSONObject> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, timeout, unit);
    }

    @Override
    public void setNotContainExpire(String key, JSONObject value) {
        ValueOperations<String, JSONObject> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    @Override
    public Set<String> keys(String key) {
        return redisTemplate.keys(key);
    }

    @Override
    public void flushDB() {
        redisTemplate.execute((RedisCallback<String>) connection -> {
            connection.flushDb();
            return "ok";
        });
    }

    @Override
    public Long dbSize() {
        return null;
    }

    @Override
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    public void receiveMessage(String message) {
        System.out.println("message = " + message);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

}
