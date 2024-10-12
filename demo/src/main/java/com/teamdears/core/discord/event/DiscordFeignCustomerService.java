package com.teamdears.core.discord.event;

import com.teamdears.core.discord.message.CustomerServiceMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${discord.customer-service}", url = "${webhooks.customer-service}")
public interface DiscordFeignCustomerService {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    void sendMessage(@RequestBody CustomerServiceMessage customerServiceMessage);

}
