package com.example.demo.review.dto;

import com.example.demo.enums.RadarKey;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

public class ReviewDTO {
    @Getter
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

    }
}
