package com.example.demo.discord;

import com.example.demo.discord.event.DiscordFeignCustomerService;
import com.example.demo.member.domain.Customer;
import com.example.demo.member.dto.MypageDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DiscordMessageProvider {

    private final DiscordFeignCustomerService discordFeignCustomerService;

    public void sendCustomerServiceMessage(Customer customer, MypageDTO.CustomerServiceRequest request) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = LocalDateTime.now().format(formatter);

        // Example embed creation
        DiscordMessage.Embed embed = new DiscordMessage.Embed(
                request.getTitle(),          // title
                request.getContent(),        // description
                4886754,                     // color (Cyan)
                new DiscordMessage.Footer(formattedTimestamp),
//                new DiscordMessage.Image("https://example.com/image.png"),
//                new DiscordMessage.Thumbnail("https://example.com/thumbnail.png"),
                new DiscordMessage.Author(customer.getName() + "(" +  customer.getUUID() + ")")
//                List.of(
//                        new DiscordMessage.Field("Date", "date", true),
//                        new DiscordMessage.Field("Time", "time", true)
//                )
        );

        // Creating a Discord message with the embed
        DiscordMessage discordMessage = DiscordMessage.createCustomerServiceMessage(
                EventMessage.CUSTOMER_SERVICE.getMessage(),
                List.of(embed)
        );

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