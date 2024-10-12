package com.teamdears.core.error;

import com.teamdears.core.enums.member.MemberRole;

public record UserInfo(
        String username,
        String UUID,
        MemberRole role
) {
    public static UserInfo createUserInfo(String username, String UUID, MemberRole role) {
        return new UserInfo(username, UUID, role);
    }
}
