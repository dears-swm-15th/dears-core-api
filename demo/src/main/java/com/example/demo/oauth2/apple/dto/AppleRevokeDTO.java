package com.example.demo.oauth2.apple.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AppleRevokeDTO {

    @Schema(type = "long", example = "3")
    private Long userId;

    @Schema(type = "string", example = "WEDDING_PLANNER")
    private String memberRole;
}
