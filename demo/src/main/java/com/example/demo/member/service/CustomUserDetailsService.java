package com.example.demo.member.service;

import com.example.demo.enums.member.MemberRole;
import com.example.demo.member.domain.Member;
import com.example.demo.member.domain.MemberContext;
import com.example.demo.member.dto.MemberAuthDTO;
import com.example.demo.member.mapper.MemberMapper;
import com.example.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository  memberRepository;

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String memberName) throws UsernameNotFoundException {
        Member member = memberRepository.findByName(memberName)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이름을 가진 사용자를 찾을 수 없습니다."));

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(member.getRole().getRoleName()));

        return new MemberContext(member, roles);
    }

    @Transactional
    public MemberAuthDTO.Response join(){
        // TODO : 웨딩플래너와 customer를 구분하여 생성하는 로직
        Member member = Member.builder()
                .role(MemberRole.CUSTOMER)
                .name(UUID.randomUUID().toString()).build();
        memberRepository.save(member);
        return memberMapper.entityToResponse(member);
    }
}
