package com.teamdears.core.oauth2.apple.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AppleRevokeDTO {

    @Schema(type = "string", example = "12321-12312-12312-12312")
    private String UUID;

    @Schema(type = "string", example = "WEDDING_PLANNER")
    private String memberRole;
}
