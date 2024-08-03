package com.example.demo.chat.dto;

import com.example.demo.enums.chat.MessageType;
import lombok.*;

public class MessageDTO {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        private MessageType messageType;

        private String contents;

        private Long chatRoomId;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;

        private MessageType messageType;

        private String contents;

        private Long chatRoomId;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PortfolioRequest {

        private MessageType messageType;

        private String contents;

        private Long portfolioId;
    }
}
