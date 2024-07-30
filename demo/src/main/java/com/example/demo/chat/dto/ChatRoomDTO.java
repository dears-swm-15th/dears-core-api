package com.example.demo.chat.dto;

import com.example.demo.chat.domain.Message;
import com.example.demo.chat.domain.ReadFlag;
import com.example.demo.enums.member.MemberRole;
import lombok.*;

import java.util.List;
import java.util.Map;

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

        private List<MessageDTO.Response> messages;

        private Long customerLastReadMessageId;

        private Long weddingPlannerLastReadMessageId;
    }
}
