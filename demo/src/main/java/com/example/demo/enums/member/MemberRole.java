package com.example.demo.enums.member;

public enum MemberRole {
    WEDDING_PLANNER,
    CUSTOMER;

    public String getRoleName() {
        return this.name();
    }
}
