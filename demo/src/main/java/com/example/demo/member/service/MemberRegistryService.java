package com.example.demo.member.service;

import com.example.demo.enums.member.MemberRole;
import com.example.demo.jwt.TokenProvider;
import com.example.demo.member.domain.Customer;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.repository.CustomerRepository;
import com.example.demo.member.repository.WeddingPlannerRepository;
import com.example.demo.oauth2.google.dto.GoogleLoginDTO;
import com.example.demo.oauth2.google.dto.GoogleUserInfoResponseDTO;
import com.example.demo.oauth2.kakao.dto.KakaoLoginDTO;
import com.example.demo.oauth2.kakao.dto.KakaoUserInfoResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberRegistryService {

    private final CustomerRepository customerRepository;
    private final WeddingPlannerRepository weddingPlannerRepository;

    private final TokenProvider tokenProvider;

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
        customer.updateRefreshToken(refreshToken);
    }

    private void createNewCustomer(String UUID, String name, String refreshToken) {
        Customer newCustomer = Customer.builder()
                .role(MemberRole.CUSTOMER)
                .name(name)
                .UUID(UUID)
                .refreshToken(refreshToken)
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
        weddingPlanner.updateRefreshToken(refreshToken);
    }

    private void createNewWeddingPlanner(String UUID, String name, String refreshToken) {
        WeddingPlanner newWeddingPlanner = WeddingPlanner.builder()
                .role(MemberRole.WEDDING_PLANNER)
                .name(name)
                .UUID(UUID)
                .refreshToken(refreshToken)
                .build();

        weddingPlannerRepository.save(newWeddingPlanner);
    }

}
