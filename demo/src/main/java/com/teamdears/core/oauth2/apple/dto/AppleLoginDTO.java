package com.teamdears.core.oauth2.apple.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class AppleLoginDTO {
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @Schema(type = "string", example = "asd2221edqsdqwd23e")
        private String appleIdToken;

        @Schema(type = "string", example = "dslafjkdsrtjlejldfkajlasljdf")
        private String authorizationCode;

        @Schema(type = "string", example = "WEDDING_PLANNER")
        private String role;
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

        @Schema(type = "string", example = "kakao-asdd2d12d")
        private String UUID;
    }
}
