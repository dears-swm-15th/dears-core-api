package com.teamdears.core.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class AuthDTO {


    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @Schema(type = "string", example = "CUSTOMER 또는 WEDDING_PLANNER")
        private String role;
    }

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        @Schema(type = "string", example = "CUSTOMER 또는 WEDDING_PLANNER")
        private String role;
        @Schema(type = "string", example = "84f6cd04-9985-4da6-94b5-e79fffd88e61")
        private String UUID;
    }

}
