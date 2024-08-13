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

        @Schema(type = "long", example = "1")
        private Long chatRoomId;

        @Schema(type = "string", example = "https://s3.ap-northeast-2.amazonaws.com/...")
        private String othersProfileImageUrl;

        @Schema(type = "string", example = "김지수")
        private String othersName;

        @Schema(type = "string", example = "넵 다음에 뵐게요.")
        private String lastMessage;

        @Schema(type = "LocalDateTime", example = "2024-07-04 16:53:33.130731")
        private LocalDateTime lastMessageCreatedAt;

        @Schema(type = "string", example = "에바웨딩스")
        private String organizationName;

        @Schema(type = "integer", example = "2")
        private Long portfolioId;

        @Schema(type = "integer", example = "2")
        private Integer unreadMessageCount;
    }
}