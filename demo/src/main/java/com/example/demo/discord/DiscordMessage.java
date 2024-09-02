package com.example.demo.discord;

public record DiscordMessage(
        String content
) {
    public static DiscordMessage createCustomerServiceMessage(String message) {
        return new DiscordMessage(message);
    }
}
