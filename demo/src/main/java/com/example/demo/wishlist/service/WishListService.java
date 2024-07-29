package com.example.demo.wishlist.service;

import com.example.demo.member.domain.Customer;
import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioOverviewDTO;
import com.example.demo.portfolio.mapper.PortfolioMapper;
import com.example.demo.portfolio.service.PortfolioService;
import com.example.demo.wishlist.domain.WishList;
import com.example.demo.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishListService {

    private final PortfolioService portfolioService;
    private final CustomUserDetailsService memberService;
    private final WishListRepository wishListRepository;
    private final PortfolioMapper portfolioMapper;

    public List<PortfolioOverviewDTO.Response> getWishListByMember(int page, int size) {
        Long memberId = memberService.getCurrentAuthenticatedCustomer().getId();
        log.info("Fetching wishlist for member ID: {}", memberId);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<WishList> wishLists = wishListRepository.findAllByCustomerId(memberId, pageRequest);
        log.info("Found {} items in wishlist for member ID: {}", wishLists.getTotalElements(), memberId);
        return wishLists.stream()
                .map(wishList -> portfolioMapper.entityToOverviewResponse(wishList.getPortfolio()))
                .toList();
    }

    @Transactional
    public void addWishList(Long portfolioId) {
        Customer customer = memberService.getCurrentAuthenticatedCustomer();
        log.info("Adding portfolio ID: {} to wishlist for customer ID: {}", portfolioId, customer.getId());
        if (wishListRepository.existsByCustomerIdAndPortfolioId(customer.getId(), portfolioId)) {
            log.warn("Portfolio ID: {} is already in the wishlist for customer ID: {}", portfolioId, customer.getId());
            return;
        }
        Portfolio portfolio = portfolioService.increaseWishListCount(portfolioId);
        WishList wishList = WishList.builder()
                .customer(customer)
                .portfolio(portfolio)
                .build();
        wishListRepository.save(wishList);
        log.info("Successfully added portfolio ID: {} to wishlist for customer ID: {}", portfolioId, customer.getId());
    }

    @Transactional
    public void deleteWishList(Long portfolioId) {
        Customer customer = memberService.getCurrentAuthenticatedCustomer();
        log.info("Deleting portfolio ID: {} from wishlist for customer ID: {}", portfolioId, customer.getId());
        if (!wishListRepository.existsByCustomerIdAndPortfolioId(customer.getId(), portfolioId)) {
            log.warn("Portfolio ID: {} is not in the wishlist for customer ID: {}", portfolioId, customer.getId());
            return;
        }
        Portfolio portfolio = portfolioService.decreaseWishListCount(portfolioId);
        wishListRepository.deleteByCustomerIdAndPortfolioId(customer.getId(), portfolio.getId());
        log.info("Successfully deleted portfolio ID: {} from wishlist for customer ID: {}", portfolioId, customer.getId());
    }
}
