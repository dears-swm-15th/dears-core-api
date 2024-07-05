package com.example.demo.portfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.example.demo.enums.review.RadarKey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PortfolioDTO {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @Schema(type = "string", example = "에바웨딩스")
        private String organization;

        @Schema(type = "string", example = "김지수")
        private String plannerName;

        @Schema(type = "string", example = "GANGNAM")
        private String region;

        @Schema(type = "string", example = "안녕하세요.")
        private String introduction;

        @Schema(type = "string", example = "010-1234-5678")
        private String contactInfo;

        @Schema(type = "string", example = "src/wedding_planner/imgs")
        private String profileImageUrl;

        @Schema(type = "integer", example = "30000")
        private Integer consultingFee;

        @Schema(type = "string", example = "웨딩 준비 도와드릴게요.")
        private String description;

        @Schema(type = "integer", example = "40000")
        private Integer avgEstimate;

        @Schema(type = "integer", example = "20000")

        private Integer minEstimate;

        @Schema(type = "array", example = "[\"퍼스널 컬러 체크\", \"웨딩드레스 시착 1회 무료\"]")
        private List<String> services;

        @Schema(type = "array", example = "[\"src/portfolio/img1.jpg\", \"src/portfolio/img2.jpg\"]")
        private List<String> weddingPhotoUrls;

        @Schema(type = "object", example = "{\"COMMUNICATION\": 4.5, \"BUDGET_COMPLIANCE\": 3.8, \"PERSONAL_CUSTOMIZATION\": 4.7, \"PRICE_NATIONALITY\": 4.0, \"SCHEDULE_COMPLIANCE\": 4.6}")
        private Map<RadarKey, Float> avgRadar;

        @Schema(type = "LocalDateTime", example = "2024-07-04 16:53:33.130731")
        private LocalDateTime createdAt;

        @Schema(type = "LocalDateTime", example = "2024-07-04 16:53:33.130731")
        private LocalDateTime updatedAt;
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

        @Schema(type = "string", example = "김지수")
        private String plannerName;

        @Schema(type = "string", example = "GANGNAM")
        private String region;

        @Schema(type = "string", example = "안녕하세요.")
        private String introduction;

        @Schema(type = "string", example = "010-1234-5678")
        private String contactInfo;

        @Schema(type = "string", example = "sdlkfjw3gee", description = "uuid로 반환됩니다. 이후 {cloudfrontURL}/{sdlkfjw3gee}로 이미지 접근 가능합니다.")
        private String profileImageUrl;

        @Schema(type = "integer", example = "30000")
        private Integer consultingFee;

        @Schema(type = "string", example = "웨딩 준비 도와드릴게요.")
        private String description;

        @Schema(type = "integer", example = "40000")
        private Integer avgEstimate;

        @Schema(type = "integer", example = "20000")

        private Integer minEstimate;

        @Schema(type = "array", example = "[\"퍼스널 컬러 체크\", \"웨딩드레스 시착 1회 무료\"]")
        private List<String> services;

        @Schema(type = "array", example = "[\"src/portfolio/img1.jpg\", \"src/portfolio/img2.jpg\"]")
        private List<String> weddingPhotoUrls;

        @Schema(type = "object", example = "{\"COMMUNICATION\": 4.5, \"BUDGET_COMPLIANCE\": 3.8, \"PERSONAL_CUSTOMIZATION\": 4.7, \"PRICE_NATIONALITY\": 4.0, \"SCHEDULE_COMPLIANCE\": 4.6}")
        private Map<RadarKey, Float> avgRadar;

        @Schema(type = "string", example = "https://s3.amazonaws.com/bucket/profileImageUrl", description = "10분동안 유효하며 PUT으로 한 개의 이미지를 전송 가능합니다.")
        private String presignedProfileImageUrl;

        @Schema(type = "array", example = "[\"https://s3.amazonaws.com/bucket/weddingPhoto1.jpg\", \"https://s3.amazonaws.com/bucket/weddingPhoto2.jpg\"]", description = "각각 10분동안 유효하며, 보낸 이미지 순서대로 presigned URL이 반환됩니다.")
        private List<String> presignedWeddingPhotoUrls;

        @Schema(type = "LocalDateTime", example = "2024-07-04 16:53:33.130731")
        private LocalDateTime createdAt;

        @Schema(type = "LocalDateTime", example = "2024-07-04 16:53:33.130731")
        private LocalDateTime updatedAt;

    }
}
