package com.teamdears.core.portfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


public class PortfolioOverviewDTO {

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        @Schema(type = "Long", example = "1")
        private Long id;

        @Schema(type = "string", example = "에바웨딩스")
        private String organization;

        @Schema(type = "string", example = "김지수")
        private String plannerName;

        @Schema(type = "string", example = "GANGNAM")
        private String region;

        @Schema(type = "string", example = "src/wedding_planner/imgs")
        private String profileImageUrl;

        @Schema(type = "integer", example = "20000")
        private Integer minEstimate;

        @Schema(type = "integer", example = "4.2")
        private Float avgRating;

        @Schema(type = "integer", example = "10")
        private Integer reviewCount;

        @Schema(type = "boolean", example = "true")
        private Boolean isWishiListed;

        public String getProfileImageUrl() {
            if (profileImageUrl == null) {
                this.profileImageUrl = "";
            }
            return profileImageUrl;
        }
    }
}
