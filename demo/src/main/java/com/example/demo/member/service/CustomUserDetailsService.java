package com.example.demo.member.service;

import com.example.demo.enums.member.MemberRole;
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

import java.lang.reflect.Member;
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
        Optional<Customer> customer = customerRepository.findByUUID(UUID);

        if (customer.isPresent()) {
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority(customer.get().getRole().getRoleName()));
            return new CustomerContext(customer.get(), roles);
        }

        // customerRepository에서 찾지 못하면 weddingPlannerRepository에서 사용자 찾기
        Optional<WeddingPlanner> planner = weddingPlannerRepository.findByUUID(UUID);

        if (planner.isPresent()) {
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority(planner.get().getRole().getRoleName()));
            return new WeddingPlannerContext(planner.get(), roles);
        }
        throw new UsernameNotFoundException("User with the given UUID could not be found");
    }

    @Transactional
    public AuthDTO.Response join(String role){

        if (role.equals(WEDDING_PLANNER.getRoleName())) {
            WeddingPlanner weddingPlanner = WeddingPlanner.builder()
                    .role(WEDDING_PLANNER)
                    .UUID(UUID.randomUUID().toString()).build();

            weddingPlannerRepository.save(weddingPlanner);
            return weddingPlannerMapper.entityToAuthDTOResponse(weddingPlanner);
        } else if (role.equals(MemberRole.CUSTOMER.getRoleName())) {
            Customer customer = Customer.builder()
                    .role(MemberRole.CUSTOMER)
                    .UUID(UUID.randomUUID().toString()).build();

            customerRepository.save(customer);
            return customerMapper.entityToAuthDTOResponse(customer);
        } else {
            throw new IllegalArgumentException("Invalid role type");
        }
    }

    public Customer getCurrentAuthenticatedCustomer() throws UsernameNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String memberName = authentication.getName();
            return customerRepository.findByUUID(memberName)
                    .orElseThrow(() -> new UsernameNotFoundException("Authenticated customer not found"));
        }
        throw new UsernameNotFoundException("Authenticated customer not found");
    }

    public WeddingPlanner getCurrentAuthenticatedWeddingPlanner() throws UsernameNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String memberName = authentication.getName();
            System.out.println(memberName);
            return weddingPlannerRepository.findByUUID(memberName)
                    .orElseThrow(() -> new UsernameNotFoundException("Authenticated weddingplanner not found"));
        }
        throw new UsernameNotFoundException("Authenticated weddingplanner not found");
    }

    public MypageDTO.CustomerResponse getCustomerMyPage() {
        Customer customer = getCurrentAuthenticatedCustomer();
        return customerMapper.entityToMypageDTOResponse(customer);
    }

    public MypageDTO.WeddingPlannerResponse getWeddingPlannerMyPage() {
        WeddingPlanner weddingPlanner = getCurrentAuthenticatedWeddingPlanner();
        return weddingPlannerMapper.entityToMypageDTOResponse(weddingPlanner);

    }
}
