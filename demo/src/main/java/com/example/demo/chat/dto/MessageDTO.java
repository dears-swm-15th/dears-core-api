package com.example.demo.chat.dto;

import com.example.demo.enums.chat.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class MessageDTO {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @Schema(type = "string", example = "ENTER")
        private MessageType messageType;

        @Schema(type = "long", example = "21")
        private Long roomId;

        @Schema(type = "long", example = "1")
        private Long senderId;

        @Schema(type = "string", example = "문의 드립니다.")
        private String content;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        @Schema(type = "string", example = "ENTER")
        private MessageType messageType;

        @Schema(type = "long", example = "21")
        private Long roomId;

        @Schema(type = "long", example = "1")
        private Long senderId;

        @Schema(type = "string", example = "문의 드립니다.")
        private String content;
    }

}