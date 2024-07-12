package com.example.demo.enums.review;

import java.util.Map;

public enum RadarKey {
    COMMUNICATION, // 의사소통
    BUDGET_COMPLIANCE, // 예산 준수
    PERSONAL_CUSTOMIZATION, // 개인 맞춤
    PRICE_NATIONALITY, // 가격 합리성
    SCHEDULE_COMPLIANCE // 일정 준수
    ;

    public static Map<RadarKey, Float> initializeRadarMap() {
        return Map.of(
                COMMUNICATION, 0f,
                BUDGET_COMPLIANCE, 0f,
                PERSONAL_CUSTOMIZATION, 0f,
                PRICE_NATIONALITY, 0f,
                SCHEDULE_COMPLIANCE, 0f);
    }
}
