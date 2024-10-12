package com.teamdears.core.member.service;

import com.teamdears.core.enums.member.MemberRole;
import com.teamdears.core.jwt.TokenProvider;
import com.teamdears.core.member.domain.Customer;
import com.teamdears.core.member.domain.WeddingPlanner;
import com.teamdears.core.member.repository.CustomerRepository;
import com.teamdears.core.member.repository.WeddingPlannerRepository;
import com.teamdears.core.oauth2.apple.domain.AppleRefreshToken;
import com.teamdears.core.oauth2.apple.dto.AppleLoginDTO;
import com.teamdears.core.oauth2.apple.dto.AppleRevokeDTO;
import com.teamdears.core.oauth2.apple.dto.AppleUserInfoResponseDTO;
import com.teamdears.core.oauth2.apple.repository.AppleRefreshTokenRepository;
import com.teamdears.core.oauth2.apple.service.AppleService;
import com.teamdears.core.oauth2.google.dto.GoogleLoginDTO;
import com.teamdears.core.oauth2.google.dto.GoogleUserInfoResponseDTO;
import com.teamdears.core.oauth2.kakao.dto.KakaoLoginDTO;
import com.teamdears.core.oauth2.kakao.dto.KakaoUserInfoResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberRegistryService {

    private final CustomerRepository customerRepository;
    private final WeddingPlannerRepository weddingPlannerRepository;

    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplateRT;
    private final NicknameService nicknameService;

    private final AppleRefreshTokenRepository appleRefreshTokenRepository;
    private final AppleService appleService;

    @Transactional
    public KakaoLoginDTO.Response createKakaoMember(KakaoUserInfoResponseDTO userInfoResponseDto, String role) {
        String username = userInfoResponseDto.kakaoAccount.profile.nickName;

        String UUID = generateUUID("kakao", userInfoResponseDto.id.toString());

        // Generate access and refresh tokens
        String accessToken = tokenProvider.createAccessToken(username, UUID);
        String refreshToken = tokenProvider.createRefreshToken(username, UUID);

        // Delegate customer or wedding planner handling based on role
        if (role.equals(MemberRole.CUSTOMER.getRoleName())) {
            processCustomer(UUID, username, refreshToken);
        } else if (role.equals(MemberRole.WEDDING_PLANNER.getRoleName())) {
            processWeddingPlanner(UUID, username, refreshToken);
        }

        return buildKakaoLoginResponse(UUID, accessToken, refreshToken);
    }

    @Transactional
    public KakaoLoginDTO.Response createAdminCustomer(String username) {
        String UUID = generateUUID("admin", "customer");

        // Generate access and refresh tokens
        String accessToken = tokenProvider.createdAdminJwtToken(username, UUID);
        String refreshToken = tokenProvider.createRefreshToken(username, UUID);

        processCustomer(UUID, username, refreshToken);

        return buildKakaoLoginResponse(UUID, accessToken, refreshToken);
    }

    @Transactional
    public KakaoLoginDTO.Response createAdminWeddingPlanner(String username) {
        String UUID = generateUUID("admin", "weddingplanner");

        // Generate access and refresh tokens
        String accessToken = tokenProvider.createdAdminJwtToken(username, UUID);
        String refreshToken = tokenProvider.createRefreshToken(username, UUID);

        processWeddingPlanner(UUID, username, refreshToken);

        return buildKakaoLoginResponse(UUID, accessToken, refreshToken);
    }

    @Transactional
    public KakaoLoginDTO.Response createTestMember(KakaoUserInfoResponseDTO userInfoResponseDto, String role) {
        String username;
        Long id;
        if (userInfoResponseDto == null) {
            username = "test";
            id = 1L;
        } else {
            username = userInfoResponseDto.kakaoAccount.profile.nickName;
            id = userInfoResponseDto.id;
        }

        String UUID = "51fc7d6b-7f86-43cf-b5c7-de4c46046d71";

        // Generate access and refresh tokens
        String accessToken = tokenProvider.createAccessToken(username, UUID);
        String refreshToken = tokenProvider.createRefreshToken(username, UUID);

        // Delegate customer or wedding planner handling based on role
        if (role.equals(MemberRole.CUSTOMER.getRoleName())) {
            processCustomer(UUID, username, refreshToken);
        } else if (role.equals(MemberRole.WEDDING_PLANNER.getRoleName())) {
            processWeddingPlanner(UUID, username, refreshToken);
        }

        return buildKakaoLoginResponse(UUID, accessToken, refreshToken);
    }


    @Transactional
    public GoogleLoginDTO.Response createGoogleMember(GoogleUserInfoResponseDTO googleUserInfoResponseDto, String role) {
        String username = googleUserInfoResponseDto.getName();
        String UUID = generateUUID("google", googleUserInfoResponseDto.getSub());

        // Generate access and refresh tokens
        String accessToken = tokenProvider.createAccessToken(username, UUID);
        String refreshToken = tokenProvider.createRefreshToken(username, UUID);

        // Delegate customer or wedding planner handling based on role
        if (role.equals(MemberRole.CUSTOMER.getRoleName())) {
            processCustomer(UUID, username, refreshToken);
        } else if (role.equals(MemberRole.WEDDING_PLANNER.getRoleName())) {
            processWeddingPlanner(UUID, username, refreshToken);
        }

        return buildGoogleLoginResponse(UUID, accessToken, refreshToken);
    }

    private String generateUUID(String platform, String id) {
        return platform + "-" + id;
    }


    private KakaoLoginDTO.Response buildKakaoLoginResponse(String UUID, String accessToken, String refreshToken) {
        return KakaoLoginDTO.Response.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .UUID(UUID)
                .build();
    }

    private GoogleLoginDTO.Response buildGoogleLoginResponse(String UUID, String accessToken, String refreshToken) {
        return GoogleLoginDTO.Response.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .UUID(UUID)
                .build();
    }

    public void processCustomer(String UUID, String name, String refreshToken) {
        Optional<Customer> findCustomer = customerRepository.findByUUID(UUID);

        if (findCustomer.isPresent()) {
            updateExistingCustomer(findCustomer.get(), refreshToken);
        } else {
            createNewCustomer(UUID, name, refreshToken);
        }
    }

    private void updateExistingCustomer(Customer customer, String refreshToken) {
        // replace refresh token by key(uuid)
        redisTemplateRT.delete(customer.getUUID());
        redisTemplateRT.opsForValue().set(customer.getUUID(), refreshToken, tokenProvider.getRefreshTokenExpiration(refreshToken), TimeUnit.MILLISECONDS);
    }

    private void createNewCustomer(String UUID, String name, String refreshToken) {
        redisTemplateRT.opsForValue().set(UUID, refreshToken, tokenProvider.getRefreshTokenExpiration(refreshToken), TimeUnit.MILLISECONDS);

        String nickname = nicknameService.generateRandomNickname();

        Customer newCustomer = Customer.builder()
                .role(MemberRole.CUSTOMER)
                .name(name)
                .nickname(nickname)
                .UUID(UUID)
                .build();

        customerRepository.save(newCustomer);
    }

    public void processWeddingPlanner(String UUID, String name, String refreshToken) {
        Optional<WeddingPlanner> findWeddingPlanner = weddingPlannerRepository.findByUUID(UUID);

        if (findWeddingPlanner.isPresent()) {
            updateExistingWeddingPlanner(findWeddingPlanner.get(), refreshToken);
        } else {
            createNewWeddingPlanner(UUID, name, refreshToken);
        }
    }

    private void updateExistingWeddingPlanner(WeddingPlanner weddingPlanner, String refreshToken) {
        // replace refresh token by key(uuid)
        redisTemplateRT.delete(weddingPlanner.getUUID());
        redisTemplateRT.opsForValue().set(weddingPlanner.getUUID(), refreshToken, tokenProvider.getRefreshTokenExpiration(refreshToken), TimeUnit.MILLISECONDS);
    }

    private void createNewWeddingPlanner(String UUID, String name, String refreshToken) {
        redisTemplateRT.opsForValue().set(UUID, refreshToken, tokenProvider.getRefreshTokenExpiration(refreshToken), TimeUnit.MILLISECONDS);

        String nickname = nicknameService.generateRandomNickname();

        WeddingPlanner newWeddingPlanner = WeddingPlanner.builder()
                .role(MemberRole.WEDDING_PLANNER)
                .name(name)
                .nickname(nickname)
                .UUID(UUID)
                .build();

        weddingPlannerRepository.save(newWeddingPlanner);
    }

    private void registerAppleRefreshToken(Long userId, String authorizationCode, String role) throws IOException {
        String refreshToken = appleService.getAppleRefreshToken(authorizationCode);

        appleRefreshTokenRepository.save(
                AppleRefreshToken.builder()
                        .userId(userId)
                        .memberRole(role)
                        .refreshToken(refreshToken)
                        .build()
        );
    }

    @Transactional
    public AppleLoginDTO.Response createAppleMember(AppleUserInfoResponseDTO userInfoResponseDTO, String role) {
        String username = userInfoResponseDTO.getAud(); // TODO
        String UUID = generateUUID("apple", userInfoResponseDTO.getSub());

        // Generate access and refresh tokens
        String accessToken = tokenProvider.createAccessToken(username, UUID);
        String refreshToken = tokenProvider.createRefreshToken(username, UUID);

        // Delegate customer or wedding planner handling based on role
        if (role.equals(MemberRole.CUSTOMER.getRoleName())) {
            processCustomer(UUID, username, refreshToken);
        } else if (role.equals(MemberRole.WEDDING_PLANNER.getRoleName())) {
            processWeddingPlanner(UUID, username, refreshToken);
        }

        // find user id by UUID
        Long userId = customerRepository.findByUUID(UUID)
                .map(Customer::getId)
                .orElseGet(() -> weddingPlannerRepository.findByUUID(UUID)
                        .map(WeddingPlanner::getId)
                        .orElseThrow(() -> new RuntimeException("User not found")));

        // register apple refresh token
        try {
            log.info("Registering apple refresh token for userId: [{}]", userId);
            registerAppleRefreshToken(userId, userInfoResponseDTO.getAuthorizationCode(), role);
        } catch (IOException e) {
            log.error("Failed to register apple refresh token", e);
            throw new RuntimeException("Failed to register apple refresh token");
        }

        return AppleLoginDTO.Response.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .UUID(UUID)
                .build();
    }

    @Transactional
    public void logout(AppleRevokeDTO revokeRequest) throws IOException {
        String refreshToken = appleRefreshTokenRepository.findByUserIdAndMemberRole(revokeRequest.getUserId(), revokeRequest.getMemberRole())
                .map(AppleRefreshToken::getRefreshToken)
                .orElseThrow(() -> new RuntimeException("Apple refresh token not found"));

        appleService.revokeToken(refreshToken);

    }
}
