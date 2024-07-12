package com.example.demo.member.service;

import com.example.demo.enums.member.MemberRole;
import com.example.demo.member.domain.Customer;
import com.example.demo.member.domain.CustomerContext;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.domain.WeddingPlannerContext;
import com.example.demo.member.dto.AuthDTO;
import com.example.demo.member.mapper.CustomerMapper;
import com.example.demo.member.mapper.WeddingPlannerMapper;
import com.example.demo.member.repository.CustomerRepository;
import com.example.demo.member.repository.WeddingPlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
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
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    private final WeddingPlannerRepository weddingPlannerRepository;

    private final CustomerMapper customerMapper;

    private final WeddingPlannerMapper weddingPlannerMapper;

    @Override
    public UserDetails loadUserByUsername(String UUID) throws UsernameNotFoundException {
        // 먼저 customerRepository에서 사용자 찾기
        Customer customer = customerRepository.findByUUID(UUID)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이름을 가진 사용자를 찾을 수 없습니다."));

        if (customer != null) {
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority(customer.getRole().getRoleName()));
            return new CustomerContext(customer, roles);
        }

        // customerRepository에서 찾지 못하면 weddingPlannerRepository에서 사용자 찾기
        WeddingPlanner planner = weddingPlannerRepository.findByUUID(UUID)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이름을 가진 사용자를 찾을 수 없습니다."));

        if (planner != null) {
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority(planner.getRole().getRoleName()));
            return new WeddingPlannerContext(planner, roles);
        }
        throw new UsernameNotFoundException("해당 이름을 가진 사용자를 찾을 수 없습니다.");
    }

    @Transactional
    public AuthDTO.Response join(String role){

        if (role.equals(WEDDING_PLANNER.getRoleName())) {
            WeddingPlanner weddingPlanner = WeddingPlanner.builder()
                    .role(WEDDING_PLANNER)
                    .UUID(UUID.randomUUID().toString()).build();

            weddingPlannerRepository.save(weddingPlanner);
            return weddingPlannerMapper.entityToResponse(weddingPlanner);
        } else if (role.equals(MemberRole.CUSTOMER.getRoleName())) {
            Customer customer = Customer.builder()
                    .role(MemberRole.CUSTOMER)
                    .UUID(UUID.randomUUID().toString()).build();

            customerRepository.save(customer);
            return customerMapper.entityToResponse(customer);
        } else {
            throw new IllegalArgumentException("잘못된 회원가입 요청입니다.");
        }
    }

    public Optional<Customer> getCurrentAuthenticatedCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String memberName = authentication.getName();
            return customerRepository.findByUUID(memberName);
        }
        throw new UsernameNotFoundException("인증된 Customer를 찾을 수 없습니다.");
    }

    public Optional<WeddingPlanner> getCurrentAuthenticatedWeddingPlanner() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String memberName = authentication.getName();
            return weddingPlannerRepository.findByUUID(memberName);
        }
        throw new UsernameNotFoundException("인증된 WeddingPlanner를 찾을 수 없습니다.");
    }
}
