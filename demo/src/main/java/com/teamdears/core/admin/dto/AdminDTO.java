package com.teamdears.core.admin.dto;

import com.teamdears.core.enums.member.MemberRole;
import lombok.*;

public class AdminDTO {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberResponse {
        private Long id;

        private MemberRole role;

        private String name;

        private String UUID;
    }


}
