package com.example.demo.member.service;

import com.example.demo.config.S3Uploader;
import com.example.demo.discord.DiscordMessageProvider;
import com.example.demo.enums.member.MemberRole;
import com.example.demo.jwt.TokenProvider;
import com.example.demo.member.domain.Customer;
import com.example.demo.member.domain.CustomerContext;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.domain.WeddingPlannerContext;
import com.example.demo.member.dto.AuthDTO;
import com.example.demo.member.dto.MypageDTO;
import com.example.demo.member.mapper.CustomerMapper;
import com.example.demo.member.mapper.WeddingPlannerMapper;
import com.example.demo.member.repository.CustomerRepository;
import com.example.demo.member.repository.WeddingPlannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.demo.enums.member.MemberRole.WEDDING_PLANNER;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final WeddingPlannerRepository weddingPlannerRepository;
    private final CustomerMapper customerMapper;
    private final WeddingPlannerMapper weddingPlannerMapper;
    private final S3Uploader s3Uploader;

    private final DiscordMessageProvider discordMessageProvider;

    private final TokenProvider tokenProvider;

    @Override
    public UserDetails loadUserByUsername(String UUID) throws UsernameNotFoundException {
        log.info("Loading user by UUID: {}", UUID);
        Optional<Customer> customer = customerRepository.findByUUID(UUID);

        if (customer.isPresent()) {
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority("ROLE_" + customer.get().getRole().getRoleName()));
            log.info("Found customer with UUID: {}", UUID);
            return new CustomerContext(customer.get(), roles);
        }

        Optional<WeddingPlanner> planner = weddingPlannerRepository.findByUUID(UUID);

        if (planner.isPresent()) {
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority("ROLE_" + planner.get().getRole().getRoleName()));
            log.info("Found wedding planner with UUID: {}", UUID);
            return new WeddingPlannerContext(planner.get(), roles);
        }

        log.error("User with UUID: {} not found", UUID);
        throw new UsernameNotFoundException("User with the given UUID could not be found");
    }

    public UserDetails loadUserByAccessToken(String accessToken) throws UsernameNotFoundException {
        log.info("Loading user by Access Token: {}", accessToken);
        String UUID = tokenProvider.getUniqueId(accessToken);
        return loadUserByUsername(UUID);
    }

    @Transactional
    public AuthDTO.Response join(String role) {
        log.info("Joining new member with role: {}", role);

        if (role.equals(WEDDING_PLANNER.getRoleName())) {
            WeddingPlanner weddingPlanner = WeddingPlanner.builder()
                    .role(WEDDING_PLANNER)
                    .UUID(UUID.randomUUID().toString()).build();

            weddingPlannerRepository.save(weddingPlanner);
            log.info("Created new wedding planner with UUID: {}", weddingPlanner.getUUID());
            return weddingPlannerMapper.entityToAuthDTOResponse(weddingPlanner);
        } else if (role.equals(MemberRole.CUSTOMER.getRoleName())) {
            Customer customer = Customer.builder()
                    .role(MemberRole.CUSTOMER)
                    .UUID(UUID.randomUUID().toString()).build();

            customerRepository.save(customer);
            log.info("Created new customer with UUID: {}", customer.getUUID());
            return customerMapper.entityToAuthDTOResponse(customer);
        } else {
            log.error("Invalid role type: {}", role);
            throw new IllegalArgumentException("Invalid role type");
        }
    }


    public Customer getCurrentAuthenticatedCustomer() throws UsernameNotFoundException {
        log.info("Getting current authenticated customer");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("AUTHENTICATION PLEASE: {}", authentication);

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String memberName = authentication.getName();
            Customer customer = customerRepository.findByUUID(memberName)
                    .orElseThrow(() -> new UsernameNotFoundException("Authenticated customer not found"));
            log.info("Authenticated customer found with UUID: {}", memberName);
            return customer;
        }
        log.error("Authenticated customer not found");
        throw new UsernameNotFoundException("Authenticated customer not found");
    }

    public WeddingPlanner getCurrentAuthenticatedWeddingPlanner() throws AuthenticationException {
        log.info("Getting current authenticated wedding planner");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String memberName = authentication.getName();
            WeddingPlanner weddingPlanner = weddingPlannerRepository.findByUUID(memberName)
                    .orElseThrow(() -> new UsernameNotFoundException("Authenticated wedding planner not found"));
            log.info("Authenticated wedding planner found with UUID: {}", memberName);
            return weddingPlanner;
        }
        log.error("Authenticated wedding planner not found");
        throw new AuthenticationException("JWT Token is not valid") {
        };
    }

    public MemberRole getCurrentAuthenticatedMemberRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return getMemberRole(authentication);
        }
        return null;
    }

    public String getCurrentAuthenticatedMemberName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            if (getCurrentAuthenticatedMemberRole() == MemberRole.CUSTOMER) {
                return getCurrentAuthenticatedCustomer().getName();
            } else {
                return getCurrentAuthenticatedWeddingPlanner().getName();
            }
        }
        return null;
    }

    public String getCurrentAuthenticatedMemberUUID() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }

    private MemberRole getMemberRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .map(this::mapRole)
                .orElse(null);
    }

    private MemberRole mapRole(String authority) {
        return "ROLE_CUSTOMER".equals(authority) ? MemberRole.CUSTOMER : MemberRole.WEDDING_PLANNER;
    }

    public MypageDTO.CustomerResponse getCustomerMyPage() {
        log.info("Getting customer my page");
        Customer customer = getCurrentAuthenticatedCustomer();
        MypageDTO.CustomerResponse response = customerMapper.entityToMypageDTOResponse(customer);
        log.info("Fetched customer my page for UUID: {}", customer.getUUID());
        return response;
    }

    public MypageDTO.WeddingPlannerResponse getWeddingPlannerMyPage() {
        log.info("Getting wedding planner my page");
        WeddingPlanner weddingPlanner = getCurrentAuthenticatedWeddingPlanner();
        MypageDTO.WeddingPlannerResponse response = weddingPlannerMapper.entityToMypageDTOResponse(weddingPlanner);
        log.info("Fetched wedding planner my page for UUID: {}", weddingPlanner.getUUID());
        return response;
    }

    public WeddingPlanner getWeddingPlannerById(Long weddingPlannerId) {
        log.info("Getting wedding planner by ID: {}", weddingPlannerId);
        WeddingPlanner weddingPlanner = weddingPlannerRepository.findById(weddingPlannerId)
                .orElseThrow(() -> new RuntimeException("WeddingPlanner not found"));
        log.info("Found wedding planner with ID: {}", weddingPlannerId);
        return weddingPlanner;
    }

    public MypageDTO.MyPageUpdateResponse updateCustomerMyPage(MypageDTO.CustomerRequest customerRequest) {
        Customer customer = getCurrentAuthenticatedCustomer();

        if (customerRequest.getName() != null && !customerRequest.getName().isEmpty()) {
            customer.setName(customerRequest.getName());
        }
        String profileImagePresignedUrl = "";
        if (customerRequest.getProfileImageUrl() != null && !customerRequest.getProfileImageUrl().isEmpty()) {

            customer.setProfileImageUrl(s3Uploader.makeUniqueFileName("mypage", customer.getId(), customerRequest.getProfileImageUrl()));
            profileImagePresignedUrl = s3Uploader.getPresignedUrl(customer.getProfileImageUrl());
        }

        MypageDTO.MyPageUpdateResponse response = customerMapper.entityToMypageUpdateResponse(customer);
        if (profileImagePresignedUrl != "") {
            response.setProfileImagePresignedUrl(profileImagePresignedUrl);
        }
        log.info("Updated customer my page for UUID: {}", customer.getUUID());
        return response;
    }

    public MypageDTO.MyPageUpdateResponse updateWeddingPlannerMyPage(MypageDTO.WeddingPlannerRequest weddingPlannerRequest) {
        WeddingPlanner weddingPlanner = getCurrentAuthenticatedWeddingPlanner();

        if (!weddingPlannerRequest.getName().isEmpty()) {
            weddingPlanner.setName(weddingPlannerRequest.getName());
        }
        String profileImagePresignedUrl = "";
        if (weddingPlannerRequest.getProfileImageUrl() != null && !weddingPlannerRequest.getProfileImageUrl().isEmpty()) {
            weddingPlanner.setProfileImageUrl(s3Uploader.makeUniqueFileName("mypage", weddingPlanner.getId(), weddingPlannerRequest.getProfileImageUrl()));
            profileImagePresignedUrl = s3Uploader.getPresignedUrl(weddingPlanner.getProfileImageUrl());
        }

        MypageDTO.MyPageUpdateResponse response = weddingPlannerMapper.entityToMypageUpdateResponse(weddingPlanner);
        if (profileImagePresignedUrl != "") {
            response.setProfileImagePresignedUrl(profileImagePresignedUrl);
        }
        log.info("Updated wedding planner my page for UUID: {}", weddingPlanner.getUUID());
        return response;
    }

    public Customer getCustomerByUuid(String uuid) {
        log.info("Getting customer by UUID: {}", uuid);
        return customerRepository.findByUUID(uuid)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public WeddingPlanner getWeddingPlannerByUuid(String uuid) {
        log.info("Getting wedding planner by UUID: {}", uuid);
        return weddingPlannerRepository.findByUUID(uuid)
                .orElseThrow(() -> new RuntimeException("WeddingPlanner not found"));
    }

    public MypageDTO.CustomerServiceResponse createCustomerService(MypageDTO.CustomerServiceRequest customerServiceRequest) {
        String username = getCurrentAuthenticatedMemberName();
        MemberRole role = getCurrentAuthenticatedMemberRole();
        String UUID = getCurrentAuthenticatedMemberUUID();

        discordMessageProvider.sendCustomerServiceMessage(username, role, UUID, customerServiceRequest);

        MypageDTO.CustomerServiceResponse response = MypageDTO.CustomerServiceResponse.builder()
                .content(customerServiceRequest.getContent())
                .build();

        log.info("Customer service request sent: {}", customerServiceRequest.getContent());

        return response;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = loadUserByUsername(tokenProvider.getUniqueId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
