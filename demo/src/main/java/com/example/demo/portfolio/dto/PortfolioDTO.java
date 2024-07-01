package com.example.demo.portfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.example.demo.enums.RadarKey;

import java.util.List;
import java.util.Map;

public class PortfolioDTO {

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @Schema(type = "string", example = "에바웨딩스")
        private String organization;

        @Schema(type = "string", example = "GANGNAM")
        private String region;

        @Schema(type = "string", example = "안녕하세요.")
        private String introduction;

        @Schema(type = "string", example = "010-1234-5678")
        private String contactInfo;

        @Schema(type = "string", example = "src/wedding_planner/imgs")
        private String profileImageUrl;

        @Schema(type = "integer", example = "30000")
        private Integer consultationFee;

        @Schema(type = "string", example = "웨딩 준비 도와드릴게요.")
        private String description;

        @Schema(type = "integer", example = "40000")
        private Integer avgFee;

        @Schema(type = "integer", example = "20000")
        private Integer minFee;

        @Schema(type = "float", example = "4.3")
        private Float rating;

        @Schema(type = "integer", example = "10")
        private Integer ratingCount;

        @Schema(type = "integer", example = "5")
        private Integer feeCount;

        @Schema(type = "integer", example = "3")
        private Integer radarCount;

        @Schema(type = "array", example = "[\"퍼스널 컬러 체크\", \"웨딩드레스 시착 1회 무료\"]")
        private List<String> services;

        @Schema(type = "array", example = "[\"src/portfolio/img1.jpg\", \"src/portfolio/img2.jpg\"]")
        private List<String> weddingPhotoUrls;

        @Schema(type = "object", example = "{\"COMMUNICATION\": 4.5, \"BUDGET_COMPLIANCE\": 3.8, \"PERSONAL_CUSTOMIZATION\": 4.7, \"PRICE_NATIONALITY\": 4.0, \"SCHEDULE_COMPLIANCE\": 4.6}")
        private Map<RadarKey, Float> radar;
    }

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        @Schema(type = "string", example = "에바웨딩스")
        private String organization;

        @Schema(type = "string", example = "GANGNAM")
        private String region;

        @Schema(type = "string", example = "안녕하세요.")
        private String introduction;

        @Schema(type = "string", example = "010-1234-5678")
        private String contactInfo;

        @Schema(type = "string", example = "src/wedding_planner/imgs")
        private String profileImageUrl;

        @Schema(type = "integer", example = "30000")
        private Integer consultationFee;

        @Schema(type = "string", example = "웨딩 준비 도와드릴게요.")
        private String description;

        @Schema(type = "integer", example = "40000")
        private Integer avgFee;

        @Schema(type = "integer", example = "20000")
        private Integer minFee;

        @Schema(type = "float", example = "4.3")
        private Float rating;

        @Schema(type = "integer", example = "10")
        private Integer ratingCount;

        @Schema(type = "integer", example = "5")
        private Integer feeCount;

        @Schema(type = "integer", example = "3")
        private Integer radarCount;

        @Schema(type = "array", example = "[\"퍼스널 컬러 체크\", \"웨딩드레스 시착 1회 무료\"]")
        private List<String> services;

        @Schema(type = "array", example = "[\"src/portfolio/img1.jpg\", \"src/portfolio/img2.jpg\"]")
        private List<String> weddingPhotoUrls;

        @Schema(type = "object", example = "{\"COMMUNICATION\": 4.5, \"BUDGET_COMPLIANCE\": 3.8, \"PERSONAL_CUSTOMIZATION\": 4.7, \"PRICE_NATIONALITY\": 4.0, \"SCHEDULE_COMPLIANCE\": 4.6}")
        private Map<RadarKey, Float> radar;
    }
}
