package com.example.demo.chat.dto;

import lombok.*;

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

        private Long weddingPlannerId;

    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
    }
}
