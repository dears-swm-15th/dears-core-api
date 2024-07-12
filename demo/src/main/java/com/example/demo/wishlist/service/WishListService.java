package com.example.demo.wishlist.service;

import com.example.demo.member.domain.Customer;
import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioOverviewDTO;
import com.example.demo.portfolio.mapper.PortfolioMapper;
import com.example.demo.portfolio.service.PortfolioService;
import com.example.demo.wishlist.domain.WishList;
import com.example.demo.wishlist.dto.WishListDto;
import com.example.demo.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class WishListService {

    private final PortfolioService portfolioService;
    private final CustomUserDetailsService memberService;
    private final WishListRepository wishListRepository;
    private final PortfolioMapper portfolioMapper;

    public List<PortfolioOverviewDTO.Response> getWishListByMember(int page, int size) {
        Long memberId = memberService.getCurrentAuthenticatedCustomer().orElseThrow().getId();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<WishList> wishLists = wishListRepository.findAllByCustomerId(memberId, pageRequest);
        List<PortfolioOverviewDTO.Response> portfolioOverviewDtos = wishLists.stream()
                .map(wishList -> portfolioMapper.entityToOverviewResponse(wishList.getPortfolio()))
                .toList();

        return portfolioOverviewDtos;
    }

    @Transactional
    public void addWishList(WishListDto.Request request) {
        Customer customer = memberService.getCurrentAuthenticatedCustomer().orElseThrow();
        Portfolio portfolio = portfolioService.addWishListCount(request.getPortfolioId());
        WishList wishList = WishList.builder()
                .customer(customer)
                .portfolio(portfolio)
                .build();
        wishListRepository.save(wishList);
    }
}
