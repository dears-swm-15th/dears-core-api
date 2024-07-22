package com.example.demo.portfolio.dto;

import com.example.demo.enums.review.RadarKey;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.Map;

public class PortfolioReviewDTO {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @Schema(type = "float", example = "3.723123")
        private Float avgRating;

        @Schema(type = "integer", example = "42")
        private Integer ratingCount;

        @Schema(type = "integer", example = "40000")
        private Integer avgEstimate;

        @Schema(type = "integer", example = "62")
        private Integer estimateCount;

        @Schema(type = "integer", example = "20000")
        private Integer minEstimate;

        @Schema(type = "object", example = "{\"COMMUNICATION\": 4.5, \"BUDGET_COMPLIANCE\": 3.8, \"PERSONAL_CUSTOMIZATION\": 4.7, \"PRICE_NATIONALITY\": 4.0, \"SCHEDULE_COMPLIANCE\": 4.6}")
        private Map<RadarKey, Float> avgRadar;

        @Schema(type = "integer", example = "62")
        private Integer radarCount;

    }

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        @Schema(type = "float", example = "3.723123")
        private Float avgRating;

        @Schema(type = "integer", example = "42")
        private Integer ratingCount;

        @Schema(type = "integer", example = "40000")
        private Integer avgEstimate;

        @Schema(type = "integer", example = "62")
        private Integer estimateCount;

        @Schema(type = "integer", example = "20000")
        private Integer minEstimate;

        @Schema(type = "object", example = "{\"COMMUNICATION\": 4.5, \"BUDGET_COMPLIANCE\": 3.8, \"PERSONAL_CUSTOMIZATION\": 4.7, \"PRICE_NATIONALITY\": 4.0, \"SCHEDULE_COMPLIANCE\": 4.6}")
        private Map<RadarKey, Float> avgRadar;

        @Schema(type = "integer", example = "62")
        private Integer radarCount;

    }
}

