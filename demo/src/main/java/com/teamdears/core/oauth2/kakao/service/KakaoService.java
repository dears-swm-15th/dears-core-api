package com.teamdears.core.oauth2.kakao.service;

import com.teamdears.core.oauth2.kakao.dto.KakaoTokenResponseDTO;
import com.teamdears.core.oauth2.kakao.dto.KakaoUserInfoResponseDTO;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class KakaoService {

    private String clientIdTest = "1d91a9b555e1b5d32675dcb7d2192a7c";
    private String clientSecretTest = "HjIvutqsy0OYQQAYqx9XpWPzR211dnd7";
    private String clientIdNative;
    private String clientId;
    private String clientSecret;
    private final String KAUTH_TOKEN_URL_HOST;
    private final String KAUTH_USER_URL_HOST;

    @Autowired
    public KakaoService(@Value("${kakao.client_id_web}") String clientId,
                        @Value("${kakao.client_secret_web}") String clientSecret,
                        @Value("${kakao.client_id_native}") String clientIdNative) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.clientIdNative = clientIdNative;
        KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
        KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
    }

    public String getAccessTokenFromKakaoNativeApp(String code) {
        KakaoTokenResponseDTO kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientIdNative)
                        .queryParam("code", code)
                        .queryParam("redirect_uri", "kakaocc81508a8ff35b0a6ce8fcfb1df48914://oauth")
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    return Mono.error(new RuntimeException("400 Invalid Parameter: " + clientResponse.statusCode()));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    return Mono.error(new RuntimeException("500 Internal Server Error: " + clientResponse.statusCode()));
                })
                .bodyToMono(KakaoTokenResponseDTO.class)
                .block();

        if (kakaoTokenResponseDto == null) {
            log.error("Failed to retrieve Kakao token response");
            throw new RuntimeException("Failed to retrieve token from Kakao");
        }

        log.info(" [ Kakao Native App ] Access Token: {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [ Kakao Native App ] Refresh Token: {}", kakaoTokenResponseDto.getRefreshToken());
        log.info(" [ Kakao Native App ] Scope: {}", kakaoTokenResponseDto.getScope());

        return kakaoTokenResponseDto.getAccessToken();
    }


    public String getAccessTokenFromKakaoDev(String code) {
        KakaoTokenResponseDTO kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientIdTest)
                        .queryParam("client_secret", clientSecretTest)
                        .queryParam("code", code)
                        .queryParam("redirect_uri", "http://localhost:8080/login/oauth2/code/kakao")
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("400 Invalid Parameter " + clientResponse.toString())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("500 Internal Server Error")))
                .bodyToMono(KakaoTokenResponseDTO.class)
                .block();


        log.info(" [ Kakao Service ] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [ Kakao Service ] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
        log.info(" [ Kakao Service ] Scope ------> {}", kakaoTokenResponseDto.getScope());

        return kakaoTokenResponseDto.getAccessToken();
    }

    public String getAccessTokenFromKakaoDeploy(String code) {
        KakaoTokenResponseDTO kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST)
                .post()
                .uri("/oauth/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("code", code)
                        // TODO : redirect_uri HTTPS 설정 후 도메인 변경
                        .with("redirect_uri", "http://dears-core-server.ap-northeast-2.elasticbeanstalk.com/login/oauth2/code/kakao"))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("KAKAO Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("500 Internal Server Error")))
                .bodyToMono(KakaoTokenResponseDTO.class)
                .block();

        log.info(" [ Kakao Service ] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [ Kakao Service ] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
        log.info(" [ Kakao Service ] Scope ------> {}", kakaoTokenResponseDto.getScope());

        return kakaoTokenResponseDto.getAccessToken();
    }

    public KakaoUserInfoResponseDTO getUserInfo(String accessToken) {

        KakaoUserInfoResponseDTO userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "KAKAO Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), "500 Internal Server Error")))
                .bodyToMono(KakaoUserInfoResponseDTO.class)
                .block();

        log.info("userInfo ---> {} ", userInfo);
        log.info(" [ Kakao Service ] Auth ID ---> {} ", userInfo.getId());
        log.info(" [ Kakao Service ] NickName ---> {} ", userInfo.getKakaoAccount().getProfile().getNickName());
        log.info(" [ Kakao Service ] ProfileImageUrl ---> {} ", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());

        return userInfo;
    }

}
