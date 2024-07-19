package com.example.demo.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

public class ChatRoomOverviewDTO {
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @Schema(type = "Long", example = "1")
        private Long chatRoomId;
    }

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private String weddingPlannerProfileImageUrl;

        private String weddingPlannerName;

        private String lastMessage;

        private LocalDateTime lastMessageCreatedAt;

        private String organizationName;

        private Long portfolioId;
    }
}