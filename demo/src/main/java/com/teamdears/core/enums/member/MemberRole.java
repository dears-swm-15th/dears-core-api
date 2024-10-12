package com.teamdears.core.enums.member;

public enum MemberRole {
    WEDDING_PLANNER,
    CUSTOMER,
    UNKNOWN;

    public String getRoleName() {
        return this.name();
    }
}
