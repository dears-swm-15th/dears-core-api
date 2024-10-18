package com.teamdears.core.oauth2.apple.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleKeyGenerator {

    //    @Value("${oauth.apple.url.public-keys}")
//    private String applePublicKeysUrl;
    private static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys"; // Apple 공개키 URL

    @Value("${apple.kid}")
    private String kid;

    @Value("${apple.team-id}")
    private String teamId;

    @Value("${apple.app-id}")
    private String appId;

    @Value("${apple.private-key}")
    private String privateKey;

    /**
     * Apple client secret 을 생성한다.
     *
     * @return client_secret JWT
     * @throws IOException
     */
    public String getClientSecret() throws IOException {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setHeaderParam("kid", kid)
                .setHeaderParam("alg", "ES256")
                .setIssuer(teamId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .setAudience("https://appleid.apple.com")
                .setSubject(appId)
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256) // 수정된 부분
                .compact();
    }

    /**
     * apple private 키를 반환한다.
     *
     * @return
     * @throws IOException
     */
    private PrivateKey getPrivateKey() throws IOException {
        try {
            log.info("pemReader: {}", privateKey.replace("\\n", "\n"));

            Reader pemReader = new StringReader(privateKey.replace("\\n", "\n"));
            PEMParser pemParser = new PEMParser(pemReader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();

            log.info("object: {}", object);

            return converter.getPrivateKey(object);
        } catch (IOException e) {
            throw new IOException("Failed to read private key", e);
        }
    }
}