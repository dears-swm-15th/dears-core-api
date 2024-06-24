package com.example.demo.portfolio;

import lombok.Data;

public class PortfolioDTO {
    @Data
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

    @Data
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
}
