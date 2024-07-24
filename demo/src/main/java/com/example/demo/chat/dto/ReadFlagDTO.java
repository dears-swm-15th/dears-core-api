package com.example.demo.chat.dto;

import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.enums.member.MemberRole;
import lombok.*;

public class ReadFlagDTO {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Long id;

        private MemberRole memberRole;

        private ChatRoom chatRoom;

        private Long lastReadMessageId;
    }


    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;

        private MemberRole memberRole;

        private Long lastReadMessageId;
    }
}
