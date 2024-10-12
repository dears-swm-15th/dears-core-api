package com.teamdears.core.member.dto;

import com.teamdears.core.enums.member.MemberRole;
import lombok.*;

public class WeddingPlannerPortfolioDTO {

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private Long id;

        private MemberRole role;

        private String name;

        private String UUID;

        private String profileImageUrl;

        public String getProfileImageUrl() {
            if (profileImageUrl == null)
            {
                this.profileImageUrl = "";
            }
            return profileImageUrl;
        }

    }
}
