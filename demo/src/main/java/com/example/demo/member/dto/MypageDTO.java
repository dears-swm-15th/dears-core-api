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

        @Schema(type = "string", example = "행복한 호랑이 123")
        private String nickname;

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

        @Schema(type = "string", example = "행복한 호랑이 123")
        private String nickname;

        @Schema(type = "string", example = "https://s3.ap-northeast-2.amazonaws.com/sopt-27th/profile/84f6cd04-9985-4da6-94b5-e79fffd88e61")
        private String profileImageUrl;

        @Schema(type = "string", example = "presignedurl")
        private String profileImagePresignedUrl;
    }


    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CustomerServiceRequest {

        @Schema(type = "string", example = "고객센터 문의합니다.")
        private String title;

        @Schema(type = "string", example = "웨딩플래너 수가 너무 부족해요.")
        private String content;

    }

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CustomerServiceResponse {

        @Schema(type = "string", example = "고객센터로 문의가 접수되었습니다.")
        private String content;

    }

}
