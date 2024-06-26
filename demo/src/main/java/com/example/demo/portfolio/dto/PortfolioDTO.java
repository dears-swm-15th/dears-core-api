package com.example.demo.portfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class PortfolioDTO {

    @Getter
    @ToString
    @NoArgsConstructor
    public static class Request {
        @Schema(example = "에바웨딩스")
        private String organization;

        @Schema(example = "GANGNAM")
        private String region;

        @Schema(example = "안녕하세요.")
        private String introduction;

        @Schema(example = "LUNCH")
        private String officeHours;

        @Schema(example = "010-1234-5678")
        private String contactInfo;

        @Schema(example = "src/wedding_planner/imgs")
        private String image;

        @Schema(example = "30000")
        private Integer consultationFee;

        @Schema(example = "웨딩 준비 도와드릴게요.")
        private String description;

        @Schema(example = "src/portfolio/imgs")
        private String weddingPhotos;
    }

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class Response {
        @Schema(example = "에바웨딩스")
        private String organization;

        @Schema(example = "GANGNAM")
        private String region;

        @Schema(example = "안녕하세요.")
        private String introduction;

        @Schema(example = "LUNCH")
        private String officeHours;

        @Schema(example = "010-1234-5678")
        private String contactInfo;

        @Schema(example = "src/wedding_planner/imgs")
        private String image;

        @Schema(example = "30000")
        private Integer consultationFee;

        @Schema(example = "웨딩 준비 도와드릴게요.")
        private String description;

        @Schema(example = "src/portfolio/imgs")
        private String weddingPhotos;
    }
}
