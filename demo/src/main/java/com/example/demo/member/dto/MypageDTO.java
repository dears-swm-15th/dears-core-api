package com.example.demo.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class MypageDTO {

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CustomerRequest {

        @Schema(type = "string", example = "84f6cd04-9985-4da6-94b5-e79fffd88e61")
        private String name;
        @Schema(type = "string", example = "https://s3.ap-northeast-2.amazonaws.com/sopt-27th/profile/84f6cd04-9985-4da6-94b5-e79fffd88e61")
        private String profileImageUrl;

    }

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WeddingPlannerRequest {

        @Schema(type = "string", example = "84f6cd04-9985-4da6-94b5-e79fffd88e61")
        private String name;
        @Schema(type = "string", example = "https://s3.ap-northeast-2.amazonaws.com/sopt-27th/profile/84f6cd04-9985-4da6-94b5-e79fffd88e61")
        private String profileImageUrl;

    }




    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CustomerResponse {

        @Schema(type = "string", example = "84f6cd04-9985-4da6-94b5-e79fffd88e61")
        private String name;
        @Schema(type = "string", example = "https://s3.ap-northeast-2.amazonaws.com/sopt-27th/profile/84f6cd04-9985-4da6-94b5-e79fffd88e61")
        private String profileImageUrl;

    }

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WeddingPlannerResponse {

        @Schema(type = "string", example = "84f6cd04-9985-4da6-94b5-e79fffd88e61")
        private String name;
        @Schema(type = "string", example = "https://s3.ap-northeast-2.amazonaws.com/sopt-27th/profile/84f6cd04-9985-4da6-94b5-e79fffd88e61")
        private String profileImageUrl;
    }

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MyPageUpdateResponse {

        @Schema(type = "string", example = "84f6cd04-9985-4da6-94b5-e79fffd88e61")
        private String name;
        @Schema(type = "string", example = "https://s3.ap-northeast-2.amazonaws.com/sopt-27th/profile/84f6cd04-9985-4da6-94b5-e79fffd88e61")
        private String profileImageUrl;
        @Schema(type = "string", example = "presignedurl")
        private String profileImagePresignedUrl;
    }


}
