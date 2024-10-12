package com.teamdears.core.oauth2.apple.dto;

public record ApplePublicKey(
        String kty,
        String kid,
        String alg,
        String n,
        String e
) {
}