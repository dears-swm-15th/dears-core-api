package com.example.demo.discord.message;

import java.util.List;

public record ExceptionMessage(
        String message,
        List<Embed> embeds
) {

    public static ExceptionMessage createExceptionMessage(String message, List<Embed> embeds) {
        return new ExceptionMessage(message, embeds);
    }

    public static record Embed(
            String title,
            String description,
            int color,
            List<Field> fields,
            Footer footer
    ) {
    }

    public static record Field(
            String name,
            String value,
            boolean inline
    ) {
    }

    public static record Footer(
            String text
    ) {
    }

}
