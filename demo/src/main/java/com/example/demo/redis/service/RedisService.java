package com.example.demo.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setValue(String key, String data) {
        ValueOperations<String, Object> value = redisTemplate.opsForValue();
        value.set(key, data);
    }

    public void setValue(String key, String data, Duration duration) {
        ValueOperations<String, Object> value = redisTemplate.opsForValue();
        value.set(key, data, duration);
    }

    @Transactional(readOnly = true)
    public String getValue(String key) {
        ValueOperations<String, Object> value = redisTemplate.opsForValue();
        if (value.get(key) == null) {
            return "false";
        }
        return (String) value.get(key);
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    public void expireValue(String key, int timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    public void setHashOps(String key, Map<String, String> data) {
        HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
        values.putAll(key, data);
    }

    @Transactional(readOnly = true)
    public String getHashOps(String key, String hashKey) {
        HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
        return Boolean.TRUE.equals(values.hasKey(key, hashKey)) ? (String) redisTemplate.opsForHash().get(key, hashKey) : "";
    }

    public void deleteHashOps(String key, String hashKey) {
        HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
        values.delete(key, hashKey);
    }

    public boolean checkExistsValue(String value) {
        return !value.equals("false");
    }

    public void setSetValue(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public String getSetValue(String key) {
        return redisTemplate.opsForSet().members(key).toString();
    }

    // set Set value with timeout
    public void setSetValue(String key, String value, Duration timeout) {
        redisTemplate.opsForSet().add(key, value);
        redisTemplate.expire(key, timeout);
    }

    public Long getSetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public boolean checkExistsSetValue(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public void deleteSetValue(String key, String value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    public void getSetsByKey(String key) {
        redisTemplate.opsForSet().members(key);
    }

    public void deleteAllSet() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    // get keys which has value
    public Set<String> getKeys(String value) {
        return redisTemplate.keys(value);
    }

    // get all keys and their corresponding set members
    public Map<String, Set<Object>> getAllSetPairs() {
        Set<String> allKeys = redisTemplate.keys("*");  // get all keys
        Map<String, Set<Object>> allSetPairs = new HashMap<>();  // create a map to store key-value pairs

        if (allKeys != null) {
            for (String key : allKeys) {
                Set<Object> members = redisTemplate.opsForSet().members(key);  // retrieve set members
                if (members != null && !members.isEmpty()) {
                    allSetPairs.put(key, members);  // store the key and its members in the map
                }
            }
        }
        return allSetPairs;
    }

}
