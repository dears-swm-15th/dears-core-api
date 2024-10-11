package com.example.demo.oauth2.apple.dto;

import lombok.Data;


@Data
public class AppleUserInfoResponseDTO {
    private String iss;
    private String aud;
    private String sub;
    private String email;
    private boolean email_verified;
    private long exp;
    private long iat;
    private String nonce;
    private boolean nonce_supported;

    private String given_name;
    private String family_name;
    private String authorizationCode;
}
