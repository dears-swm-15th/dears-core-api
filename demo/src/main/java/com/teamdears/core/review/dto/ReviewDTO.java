package com.teamdears.core.review.dto;

import com.teamdears.core.enums.review.RadarKey;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        private String content;

        @Schema(type = "boolean", example = "true")
        private Boolean isProvided;

        @Schema(type = "float", example = "4.5")
        private Float rating;

        @Schema(type = "integer", example = "1500000")
        private Integer estimate;

        @Schema(type = "integer", example = "1000000")
        private Integer minEstimate;

        @Schema(type = "array", example = "[\"비동행\", \"신혼여행\"]")
        private List<String> tags;

        @Schema(type = "array", example = "[\"src/portfolio/23/img1.jpg\", \"src/portfolio/23/img2.jpg\"]")
        private List<String> weddingPhotoUrls;

        @Schema(type = "object", example = "{\"COMMUNICATION\": 4.5, \"BUDGET_COMPLIANCE\": 3.8, \"PERSONAL_CUSTOMIZATION\": 4.7, \"PRICE_RATIONALITY\": 4.0, \"SCHEDULE_COMPLIANCE\": 4.6}")
        private Map<RadarKey, Float> radar;

        @Schema(type = "integer", example = "2")
        private Long portfolioId;

    }

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        @Schema(type = "long", example = "1")
        private Long id;

        @Schema(type = "long", example = "1")
        private Long portfolioId;

        @Schema(type = "string", example = "결혼하고 싶어요123")
        private String reviewerName;

        @Schema(type = "string", example = "최고의 웨딩플래너!")
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

        @Schema(type = "array", example = "[\"https://s3.amazonaws.com/bucket/weddingPhoto1.jpg\", \"https://s3.amazonaws.com/bucket/weddingPhoto2.jpg\"]", description = "각각 10분동안 유효하며, 보낸 이미지 순서대로 presigned URL이 반환됩니다.")
        private List<String> presignedWeddingPhotoUrls;

        @Schema(type = "object", example = "{\"COMMUNICATION\": 4.5, \"BUDGET_COMPLIANCE\": 3.8, \"PERSONAL_CUSTOMIZATION\": 4.7, \"PRICE_RATIONALITY\": 4.0, \"SCHEDULE_COMPLIANCE\": 4.6}")
        private Map<RadarKey, Float> radar;

        @Schema(type = "LocalDateTime", example = "2024-07-04 16:53:33.130731")
        private LocalDateTime createdAt;

        @Schema(type = "LocalDateTime", example = "2024-07-04 16:53:33.130731")
        private LocalDateTime updatedAt;

        public List<String> getPresignedWeddingPhotoUrls() {
            if (this.presignedWeddingPhotoUrls == null) {
                this.presignedWeddingPhotoUrls = new ArrayList<>();
            }
            return this.presignedWeddingPhotoUrls;
        }

        public List<String> getWeddingPhotoUrls() {
            if (this.weddingPhotoUrls == null) {
                this.weddingPhotoUrls = new ArrayList<>();
            }
            return this.weddingPhotoUrls;
        }
    }
}
