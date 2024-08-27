package com.example.demo.discord;

import java.util.List;

public record DiscordMessage(
        String content,
        List<Embed> embeds
) {

    public static DiscordMessage createCustomerServiceMessage(String message, List<Embed> embeds) {
        return new DiscordMessage(message, embeds);
    }

    public static record Embed(
            String title,
            String description,
            int color,
            Footer footer,
//            Image image,
//            Thumbnail thumbnail,
            Author author
//            List<Field> fields
    ) {
    }

    public static record Footer(
            String text
    ) {
    }

    public static record Image(
            String url
    ) {
    }

    public static record Thumbnail(
            String url
    ) {
    }

    public static record Author(
            String name
    ) {
    }

    public static record Field(
            String name,
            String time,
            boolean inline
    ) {
    }
}
