package com.example.demo.chat.dto;

import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        private List<MessageDTO.Response> messages;

        private HashSet<String> userIds;
    }
}
