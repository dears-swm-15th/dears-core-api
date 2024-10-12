package com.teamdears.core.enums.member;

public enum MatchingStatus {

    MATCHED, // 매칭 완료
    CANCELED, // 매칭 취소
    COMPLETED; // 플래닝 완료

    public String getStatusName() {
        return name();
    }
}
