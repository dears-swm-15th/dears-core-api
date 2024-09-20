package com.example.demo.oauth2.kakao.dto;

import com.example.demo.enums.member.MemberRole;
import com.example.demo.member.domain.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginMemberResponseDto {

    private String name;

    private String uniqueId;

    private String refreshToken;

    private String accessToken;

    private MemberRole memberRole;

    // Static factory method
    public static LoginMemberResponseDto fromCustomer(Customer customer, String accessToken) {
        return new LoginMemberResponseDto(
                customer.getName(),
                customer.getUUID(),
                accessToken, customer.getRefreshToken(),
                customer.getRole()
        );
    }
}
