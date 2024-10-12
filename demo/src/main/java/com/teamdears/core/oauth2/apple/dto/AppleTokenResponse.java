package com.teamdears.core.oauth2.apple.dto;

import lombok.Data;

@Data
public class AppleTokenResponse {
    private String access_token;
    private long expires_in;
    private String id_token;
    private String refresh_token;
    private String token_type;
    private String error;
}
