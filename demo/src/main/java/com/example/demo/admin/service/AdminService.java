package com.example.demo.admin.service;

import com.example.demo.admin.dto.AdminDTO;
import com.example.demo.enums.member.MemberRole;
import com.example.demo.jwt.TokenProvider;
import com.example.demo.member.domain.Customer;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.repository.CustomerRepository;
import com.example.demo.member.repository.WeddingPlannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final CustomerRepository customerRepository;
    private final WeddingPlannerRepository weddingPlannerRepository;

    private final TokenProvider tokenProvider;

    public List<AdminDTO.MemberResponse> getAllMembers() {
        List<Customer> customers = customerRepository.findAll();
        List<WeddingPlanner> weddingPlanners = weddingPlannerRepository.findAll();

        // Map customers to MemberResponse
        List<AdminDTO.MemberResponse> customerResponses = customers.stream()
                .map(customer -> AdminDTO.MemberResponse.builder()
                        .id(customer.getId())
                        .role(MemberRole.CUSTOMER)  // Assuming role is set like this
                        .name(customer.getName())
                        .UUID(customer.getUUID())   // Assuming Customer has a getUUID method
                        .build())
                .collect(Collectors.toList());

        // Map wedding planners to MemberResponse
        List<AdminDTO.MemberResponse> weddingPlannerResponses = weddingPlanners.stream()
                .map(weddingPlanner -> AdminDTO.MemberResponse.builder()
                        .id(weddingPlanner.getId())
                        .role(MemberRole.WEDDING_PLANNER)  // Assuming role is set like this
                        .name(weddingPlanner.getName())
                        .UUID(weddingPlanner.getUUID())    // Assuming WeddingPlanner has a getUUID method
                        .build())
                .collect(Collectors.toList());

        // Combine both lists
        List<AdminDTO.MemberResponse> allMembers = new ArrayList<>();
        allMembers.addAll(customerResponses);
        allMembers.addAll(weddingPlannerResponses);

        return allMembers;
    }
    
}

