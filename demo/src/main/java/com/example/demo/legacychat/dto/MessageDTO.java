package com.example.demo.legacychat.dto;

import com.example.demo.enums.chat.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;
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

        @Schema(type = "long", example = "21")
        private Long roomId;

        @Schema(type = "long", example = "1")
        private Long senderId;

        @Schema(type = "string", example = "문의 드립니다.")
        private String contents;

        @Schema(type = "boolean", example = "false")
        private Boolean isOtherRead;

        @Schema(type = "LocalDateTime", example = "2024-07-04 16:53:33.130731")
        private LocalDateTime createdAt;

    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        @Schema(type = "long", example = "21")
        private Long roomId;

        @Schema(type = "long", example = "1")
        private Long senderId;

        @Schema(type = "string", example = "문의 드립니다.")
        private String contents;

        @Schema(type = "boolean", example = "false")
        private Boolean isOtherRead;

        @Schema(type = "LocalDateTime", example = "2024-07-04 16:53:33.130731")
        private LocalDateTime createdAt;

    }

}