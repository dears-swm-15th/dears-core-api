package com.teamdears.core.oauth2.apple.service;

import com.teamdears.core.jwt.JwtValidator;
import com.teamdears.core.oauth2.apple.dto.ApplePublicKeyResponse;
import com.teamdears.core.oauth2.apple.dto.AppleTokenResponse;
import com.teamdears.core.oauth2.apple.dto.AppleUserInfoResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleService {

    private final JwtValidator jwtValidator;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final AppleKeyGenerator appleKeyGenerator;

    RestClient restClient = RestClient.create();

    @Value("${apple.issuer}")
    private String issuer;

    @Value("${apple.client-id}")
    private String clientId;

    private static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys";

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public AppleUserInfoResponseDTO getAppleUserInfo(String identityToken) throws
            IOException,
            NoSuchAlgorithmException,
            InvalidKeySpecException, AuthenticationException, InterruptedException, org.apache.http.auth.AuthenticationException {

        // jwt 헤더를 파싱한다.
        Map<String, String> headers = jwtValidator.parseHeaders(identityToken);

        // 공개키를 생성한다
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(headers, getAppleAuthPublicKey());

        // 공개 키를 사용해 토큰의 서명을 검사하고 Claim을 반환받는다.
        Claims tokenClaims = jwtValidator.getTokenClaims(identityToken, publicKey);

        log.info("headers: {}", headers);
        log.info("tokenClaims: {}", tokenClaims);

        verifyIdentityToken(tokenClaims);
        AppleUserInfoResponseDTO response = objectMapper.convertValue(tokenClaims, AppleUserInfoResponseDTO.class);

        return response;
    }

    private void verifyIdentityToken(Claims tokenClaims) {
        if (!issuer.equals(tokenClaims.getIssuer()))
            throw new MalformedJwtException("Invalid JWT");

        if (!clientId.equals(tokenClaims.getAudience()))
            throw new MalformedJwtException("Invalid JWT");
    }

    private ApplePublicKeyResponse getAppleAuthPublicKey() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(APPLE_PUBLIC_KEYS_URL))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ApplePublicKeyResponse.class);
        } else {
            throw new RuntimeException("Failed to retrieve Apple public keys. Status code: " + response.statusCode());
        }
    }

    public String getAppleRefreshToken(String authorizationCode) throws IOException {
        log.info("getAppleRefreshToken authorizationCode: {}", authorizationCode);
        MultiValueMap<String, String> body = getCreateTokenBody(authorizationCode);

        log.info("Request Body: {}", body);

        AppleTokenResponse appleTokenResponse = restClient.post()
                .uri("https://appleid.apple.com/auth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(AppleTokenResponse.class);

        return Objects.requireNonNull(appleTokenResponse).getRefresh_token();
    }

    private MultiValueMap<String, String> getCreateTokenBody(String authorizationCode) throws IOException {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("client_id", clientId);
        body.add("client_secret", appleKeyGenerator.getClientSecret());
        body.add("grant_type", "authorization_code");
        return body;
    }

    public void revokeToken(String refreshToken) throws IOException {
        MultiValueMap<String, String> body = getRevokeTokenBody(refreshToken);

        restClient.post()
                .uri("https://appleid.apple.com/auth/revoke")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    private MultiValueMap<String, String> getRevokeTokenBody(String refreshToken) throws IOException {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("token", refreshToken);
        body.add("client_secret", appleKeyGenerator.getClientSecret());
        body.add("token_type_hint", "refresh_token");
        return body;
    }
}
