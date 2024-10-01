package com.example.demo.enums.portfolio;

public enum Region {

    SEOUL("서울"),
    INCHEON("인천"),
    GYEONGGI("경기");

    private final String region;

    Region(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }
}
