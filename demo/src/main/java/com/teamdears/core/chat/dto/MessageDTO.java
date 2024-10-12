package com.teamdears.core.chat.dto;

import com.teamdears.core.enums.chat.MessageType;
import com.teamdears.core.enums.member.MemberRole;
import lombok.*;

import java.time.LocalDateTime;

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

        private String content;

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

        private String content;

        private LocalDateTime createdAt;
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
