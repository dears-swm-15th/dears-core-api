package com.example.demo.discord.message;

import java.util.List;

public record CustomerServiceMessage(
        String message,
        List<Embed> embeds
) {

    public static CustomerServiceMessage createCustomerServiceMessage(String message, List<Embed> embeds) {
        return new CustomerServiceMessage(message, embeds);
    }

    public static record Embed(
            String title,
            String description,
            int color,
//            Image image,
//            Thumbnail thumbnail,
            Author author,
            List<Field> fields,
            Footer footer
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
            String value,
            boolean inline
    ) {
    }


}
