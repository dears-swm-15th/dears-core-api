package com.example.demo.admin.dto;

import com.example.demo.enums.member.MemberRole;
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
