package com.example.demo.redis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RedisServiceTest {

    private RedisService redisService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        redisService = new RedisService(redisTemplate);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    }

    @Test
    public void testSetValue() {
        String key = "testKey";
        String data = "testData";

        redisService.setValue(key, data);

        verify(valueOperations, times(1)).set(key, data);
    }

    @Test
    public void testSetValueWithDuration() {
        String key = "testKey";
        String data = "testData";
        Duration duration = Duration.ofMinutes(5);

        redisService.setValue(key, data, duration);

        verify(valueOperations, times(1)).set(key, data, duration);
    }

    @Test
    public void testGetValue() {
        String key = "testKey";
        String data = "testData";

        when(valueOperations.get(key)).thenReturn(data);

        String result = redisService.getValue(key);

        assertEquals(data, result);
    }

    @Test
    public void testGetValueReturnsFalseWhenNull() {
        String key = "testKey";

        when(valueOperations.get(key)).thenReturn(null);

        String result = redisService.getValue(key);

        assertEquals("false", result);
    }

    @Test
    public void testDeleteValue() {
        String key = "testKey";

        redisService.deleteValue(key);

        verify(redisTemplate, times(1)).delete(key);
    }

    @Test
    public void testExpireValue() {
        String key = "testKey";
        int timeout = 5000;

        redisService.expireValue(key, timeout);

        verify(redisTemplate, times(1)).expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testSetHashOps() {
        String key = "testKey";
        Map<String, String> data = new HashMap<>();
        data.put("hashKey1", "value1");
        data.put("hashKey2", "value2");

        redisService.setHashOps(key, data);

        verify(hashOperations, times(1)).putAll(key, data);
    }

    @Test
    public void testGetHashOps() {
        String key = "testKey";
        String hashKey = "hashKey";
        String data = "testData";

        when(hashOperations.hasKey(key, hashKey)).thenReturn(true);
        when(hashOperations.get(key, hashKey)).thenReturn(data);

        String result = redisService.getHashOps(key, hashKey);

        assertEquals(data, result);
    }

    @Test
    public void testGetHashOpsReturnsEmptyStringWhenNotFound() {
        String key = "testKey";
        String hashKey = "hashKey";

        when(hashOperations.hasKey(key, hashKey)).thenReturn(false);

        String result = redisService.getHashOps(key, hashKey);

        assertEquals("", result);
    }

    @Test
    public void testDeleteHashOps() {
        String key = "testKey";
        String hashKey = "hashKey";

        redisService.deleteHashOps(key, hashKey);

        verify(hashOperations, times(1)).delete(key, hashKey);
    }

    @Test
    public void testCheckExistsValue() {
        String value = "testValue";

        boolean result = redisService.checkExistsValue(value);

        assertTrue(result);
    }

    @Test
    public void testCheckExistsValueReturnsFalseForFalseString() {
        String value = "false";

        boolean result = redisService.checkExistsValue(value);

        assertFalse(result);
    }
}
