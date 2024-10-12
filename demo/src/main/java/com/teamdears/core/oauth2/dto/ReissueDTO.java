package com.teamdears.core.oauth2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class ReissueDTO {

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @Schema(type = "string", example = "asd2221edqsdqwd23e")
        private String refreshToken;
    }


    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        @Schema(type = "string", example = "asd2221edqsdqwd23e")
        private String accessToken;

        @Schema(type = "string", example = "asd2221edqsdqwd23e")
        private String refreshToken;
    }
}
