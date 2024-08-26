package com.example.demo.discord;

import com.example.demo.discord.event.DiscordFeignCustomerService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.demo.discord.DiscordMessage.*;

@Component
@RequiredArgsConstructor
public class DiscordMessageProvider {

    private final DiscordFeignCustomerService discordFeignCustomerService;

    public void sendCustomerServiceMessage(String message) {
        DiscordMessage discordMessage = createCustomerServiceMessage(message);
        sendCustomerMessageToDiscord(discordMessage);
    }

    private void sendCustomerMessageToDiscord(DiscordMessage discordMessage) {
        try {
            discordFeignCustomerService.sendMessage(discordMessage);
        } catch (FeignException e) {
            throw new RuntimeException("ErrorMessage: INVALID_DISCORD_MESSAGE");
        }
    }



}