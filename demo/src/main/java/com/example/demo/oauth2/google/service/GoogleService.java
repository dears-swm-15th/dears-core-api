package com.example.demo.oauth2.google.service;

import com.example.demo.oauth2.google.dto.GoogleUserInfoResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleService {

    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_MEMBER_INFO_URL = "https://oauth2.googleapis.com/tokeninfo";

    public GoogleUserInfoResponseDTO getGoogleMemberInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        log.info("accessToken : {}", accessToken);

        HashMap<String, String> accessTokenMap = new HashMap<>();
        accessTokenMap.put("access_token", accessToken);

        GoogleUserInfoResponseDTO memberInfo = restTemplate.postForObject(GOOGLE_MEMBER_INFO_URL, accessTokenMap, GoogleUserInfoResponseDTO.class);

        return memberInfo;
    }
}
