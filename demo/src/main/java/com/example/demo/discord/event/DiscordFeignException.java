package com.example.demo.discord.event;

import com.example.demo.discord.message.ExceptionMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${discord.exception}", url = "${webhooks.exception}")
public interface DiscordFeignException {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    void sendMessage(@RequestBody ExceptionMessage exceptionMessage);
}
