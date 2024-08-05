package com.example.demo.chat.dto;

import lombok.*;

import java.util.List;

public class ChatRoomDTO {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Long id;

        private Long portfolioId;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;

        private List<MessageDTO.Response> messages;

        private Long customerLastReadMessageId;

        private Long weddingPlannerLastReadMessageId;
    }
}
