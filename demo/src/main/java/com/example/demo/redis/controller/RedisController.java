package com.example.demo.redis.controller;

import com.example.demo.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    @PostMapping("/set")
    public void setValue(@RequestParam String key, @RequestParam String value) {
        log.info("Setting value for key: {}", key);
        redisService.setValue(key, value);
        log.info("Value set for key: {}", key);
    }

    @PostMapping("/setWithExpiry")
    public void setValueWithExpiry(@RequestParam String key, @RequestParam String value, @RequestParam long durationInSeconds) {
        log.info("Setting value with expiry for key: {} for duration: {} seconds", key, durationInSeconds);
        redisService.setValue(key, value, Duration.ofSeconds(durationInSeconds));
        log.info("Value with expiry set for key: {}", key);
    }

    @GetMapping("/get")
    public String getValue(@RequestParam String key) {
        log.info("Getting value for key: {}", key);
        String value = redisService.getValue(key);
        log.info("Retrieved value for key: {}", key);
        return value;
    }

    @DeleteMapping("/delete")
    public void deleteValue(@RequestParam String key) {
        log.info("Deleting value for key: {}", key);
        redisService.deleteValue(key);
        log.info("Deleted value for key: {}", key);
    }

    @PostMapping("/expire")
    public void expireValue(@RequestParam String key, @RequestParam int timeout) {
        log.info("Setting expire timeout for key: {} to: {} milliseconds", key, timeout);
        redisService.expireValue(key, timeout);
        log.info("Expire timeout set for key: {}", key);
    }

    @PostMapping("/setHash")
    public void setHashOps(@RequestParam String key, @RequestBody Map<String, String> data) {
        log.info("Setting hash ops for key: {} with data: {}", key, data);
        redisService.setHashOps(key, data);
        log.info("Hash ops set for key: {}", key);
    }

    @GetMapping("/getHash")
    public String getHashOps(@RequestParam String key, @RequestParam String hashKey) {
        log.info("Getting hash ops for key: {} and hashKey: {}", key, hashKey);
        String value = redisService.getHashOps(key, hashKey);
        log.info("Retrieved hash ops for key: {} and hashKey: {}", key, hashKey);
        return value;
    }

    @DeleteMapping("/deleteHash")
    public void deleteHashOps(@RequestParam String key, @RequestParam String hashKey) {
        log.info("Deleting hash ops for key: {} and hashKey: {}", key, hashKey);
        redisService.deleteHashOps(key, hashKey);
        log.info("Deleted hash ops for key: {} and hashKey: {}", key, hashKey);
    }

    @GetMapping("/exists")
    public boolean checkExistsValue(@RequestParam String key) {
        log.info("Checking existence for key: {}", key);
        boolean exists = redisService.checkExistsValue(redisService.getValue(key));
        log.info("Existence check for key: {} is: {}", key, exists);
        return exists;
    }
}
