package com.example.demo.wishlist;


import com.example.demo.config.S3Config;
import com.example.demo.config.S3Uploader;
import com.example.demo.enums.member.MemberRole;
import com.example.demo.member.domain.Member;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.repository.PortfolioRepository;
import com.example.demo.wishlist.domain.WishList;
import com.example.demo.wishlist.dto.WishListDto;
import com.example.demo.wishlist.repository.WishListRepository;
import com.example.demo.wishlist.service.WishListService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.testng.Assert.assertEquals;

@SpringBootTest
@Transactional
public class WishListTest {

    @MockBean
    private S3Uploader s3Uploader;

    @MockBean
    private S3Config s3Config;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private WishListService wishListService;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void before() {

        // 임시 사용자 생성 및 인증 설정
        Member member = new Member(1L, MemberRole.CUSTOMER, "test");
        UserDetails userDetails = new User(
                member.getName(),
                "password",
                Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getRoleName()))
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, "password", userDetails.getAuthorities())
        );
        memberRepository.saveAndFlush(member);

        Member member1 = memberRepository.findById(1L).orElseThrow();
        Member member2 = memberRepository.findById(2L).orElseThrow();
        Portfolio portfolio1 = portfolioRepository.findById(1L).orElseThrow();
        Portfolio portfolio2 = portfolioRepository.findById(2L).orElseThrow();

        wishListRepository.saveAndFlush(WishList.builder()
                .member(member1)
                .portfolio(portfolio1)
                .build());
    }

    @AfterEach
    public void after() {
        wishListRepository.deleteAll();
    }

    @Test
    @DisplayName("WishList_등록_시_Portfolio_WishListCount_증가")
    public void WishList_등록_시_Portfolio_WishListCount_증가() {
        wishListService.addWishList(WishListDto.Request.builder()
                .portfolioId(1L)
                .build());

        assertEquals(1, wishListRepository.findById(1L).orElseThrow().getPortfolio().getWishListCount());
    }

}
