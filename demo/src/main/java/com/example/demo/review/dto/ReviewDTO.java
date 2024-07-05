package com.example.demo.review.dto;

import com.example.demo.enums.review.RadarKey;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReviewDTO {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @Schema(type = "string", example = "name1")
        private String reviewerName;

        @Schema(type = "string", example = "name1")
        private String content;

        @Schema(type = "boolean", example = "true")
        private Boolean isProvided;

        @Schema(type = "float", example = "4.5")
        private Float rating;

        @Schema(type = "integer", example = "1500000")
        private Integer estimate;

        @Schema(type = "array", example = "[\"비동행\", \"신혼여행\"]")
        private List<String> tags;

        @Schema(type = "array", example = "[\"src/portfolio/23/img1.jpg\", \"src/portfolio/23/img2.jpg\"]")
        private List<String> weddingPhotoUrls;

        @Schema(type = "object", example = "{\"COMMUNICATION\": 4.5, \"BUDGET_COMPLIANCE\": 3.8, \"PERSONAL_CUSTOMIZATION\": 4.7, \"PRICE_NATIONALITY\": 4.0, \"SCHEDULE_COMPLIANCE\": 4.6}")
        private Map<RadarKey, Float> radar;

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

        @Schema(type = "string", example = "name1")
        private String reviewerName;

        @Schema(type = "string", example = "name1")
        private String content;

        @Schema(type = "boolean", example = "true")
        private Boolean isProvided;

        @Schema(type = "float", example = "4.5")
        private Float rating;

        @Schema(type = "integer", example = "1500000")
        private Integer estimate;

        @Schema(type = "array", example = "[\"비동행\", \"신혼여행\"]")
        private List<String> tags;

        @Schema(type = "array", example = "[\"src/portfolio/23/img1.jpg\", \"src/portfolio/23/img2.jpg\"]")
        private List<String> weddingPhotoUrls;

        @Schema(type = "object", example = "{\"COMMUNICATION\": 4.5, \"BUDGET_COMPLIANCE\": 3.8, \"PERSONAL_CUSTOMIZATION\": 4.7, \"PRICE_NATIONALITY\": 4.0, \"SCHEDULE_COMPLIANCE\": 4.6}")
        private Map<RadarKey, Float> radar;

        @Schema(type = "array", example = "[\"https://s3.amazonaws.com/bucket/weddingPhoto1.jpg\", \"https://s3.amazonaws.com/bucket/weddingPhoto2.jpg\"]", description = "각각 10분동안 유효하며, 보낸 이미지 순서대로 presigned URL이 반환됩니다.")
        private List<String> presignedWeddingPhotoUrls;

        @Schema(type = "LocalDateTime", example = "2024-07-04 16:53:33.130731")
        private LocalDateTime createdAt;

        @Schema(type = "LocalDateTime", example = "2024-07-04 16:53:33.130731")
        private LocalDateTime updatedAt;

        @Schema(type = "string", example = "24.07.04")
        private String wroteAt;
    }
}
