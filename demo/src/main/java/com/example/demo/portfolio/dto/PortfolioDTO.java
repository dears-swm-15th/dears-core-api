package com.example.demo.portfolio.dto;

import lombok.*;

public class PortfolioDTO {

    @Getter
    @ToString
    @NoArgsConstructor
    public static class Request {
        private String organization;
        private String region;
        private String introduction;
        private String officeHours;
        private String contactInfo;
        private String image;
        private Integer consultationFee;
        private String description;
        private String weddingPhotos;
    }

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private String organization;
        private String region;
        private String introduction;
        private String officeHours;
        private String contactInfo;
        private String image;
        private Integer consultationFee;
        private String description;
        private String weddingPhotos;
    }
}
