package com.example.demo.portfolio.dto;

import com.example.demo.enums.review.RadarKey;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.Map;

public class PortfolioSearchDTO {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @Schema(type = "Long", example = "1")
        private Long id;

        @Schema(type = "string", example = "에바웨딩스")
        private String organization;

        @Schema(type = "string", example = "김지수")
        private String plannerName;

        @Schema(type = "string", example = "GANGNAM")
        private String region;

        @Schema(type = "string", example = "안녕하세요.")
        private String introduction;

        @Schema(type = "integer", example = "30000")
        private Integer consultingFee;

        @Schema(type = "string", example = "웨딩 준비 도와드릴게요.")
        private String description;

        @Schema(type = "float", example = "3.723123")
        private Float ratingSum;

        @Schema(type = "integer", example = "42")
        private Integer ratingCount;

        @Schema(type = "integer", example = "40000")
        private Integer avgEstimate;

        @Schema(type = "integer", example = "62")
        private Integer estimateSum;

        @Schema(type = "integer", example = "20000")
        private Integer minEstimate;

        @Schema(type = "array", example = "[\"퍼스널 컬러 체크\", \"웨딩드레스 시착 1회 무료\"]")
        private List<String> services;

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
    }

}
