package com.example.demo.chat.dto;

import com.example.demo.enums.chat.MessageType;
import com.example.demo.enums.member.MemberRole;
import lombok.*;

public class MessageDTO {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private MemberRole senderRole;

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

        private MemberRole senderRole;

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
    public static class UnreadMessageResponse {

        private int unreadMessageCount;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PortfolioRequest {

        private MessageType messageType;

        private Long portfolioId;
    }
}
