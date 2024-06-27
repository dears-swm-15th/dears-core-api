package com.example.demo.portfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class PortfolioDTO {

    @Getter
    @ToString
    @NoArgsConstructor
    public static class Request {

        @Schema(type = "string", example = "에바웨딩스")
        private String organization;

        @Schema(type = "string", example = "GANGNAM")
        private String region;

        @Schema(type = "string", example = "안녕하세요.")
        private String introduction;

        @Schema(type = "string", example = "LUNCH")
        private String officeHours;

        @Schema(type = "string", example = "010-1234-5678")
        private String contactInfo;

        @Schema(type = "string", example = "src/wedding_planner/imgs")
        private String image;

        @Schema(type = "integer", example = "30000")
        private Integer consultationFee;

        @Schema(type = "string", example = "웨딩 준비 도와드릴게요.")
        private String description;

        @Schema(type = "string", example = "src/portfolio/imgs")
        private String weddingPhotos;
    }

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class Response {
        @Schema(type = "string", example = "에바웨딩스")
        private String organization;

        @Schema(type = "string", example = "GANGNAM")
        private String region;

        @Schema(type = "string", example = "안녕하세요.")
        private String introduction;

        @Schema(type = "string", example = "LUNCH")
        private String officeHours;

        @Schema(type = "string", example = "010-1234-5678")
        private String contactInfo;

        @Schema(type = "string", example = "src/wedding_planner/imgs")
        private String image;

        @Schema(type = "integer", example = "30000")
        private Integer consultationFee;

        @Schema(type = "string", example = "웨딩 준비 도와드릴게요.")
        private String description;

        @Schema(type = "string", example = "src/portfolio/imgs")
        private String weddingPhotos;
    }
}
