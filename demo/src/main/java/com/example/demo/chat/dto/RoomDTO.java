package com.example.demo.chat.dto;

import com.example.demo.chat.domain.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class RoomDTO {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @Schema(type = "List<Message>", example = "")
        private List<Message> messages;

        @Schema(type = "long", example = "1")
        private Long memberId;

        @Schema(type = "long", example = "21")
        private Long weddingPlannerId;

        @Schema(type = "LocalDateTime", example = "2024-07-04 16:53:33.130731")
        private LocalDateTime createdAt;

        @Schema(type = "LocalDateTime", example = "2024-07-04 16:53:33.130731")
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        @Schema(type = "List<Message>", example = "")
        private List<Message> messages;

        @Schema(type = "long", example = "1")
        private Long memberId;

        @Schema(type = "long", example = "21")
        private Long weddingPlannerId;

        @Schema(type = "LocalDateTime", example = "2024-07-04 16:53:33.130731")
        private LocalDateTime createdAt;

        @Schema(type = "LocalDateTime", example = "2024-07-04 16:53:33.130731")
        private LocalDateTime updatedAt;
        
    }

}