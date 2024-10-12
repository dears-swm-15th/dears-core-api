package com.teamdears.core.wishlist.service;

import com.teamdears.core.member.domain.Customer;
import com.teamdears.core.member.service.CustomUserDetailsService;
import com.teamdears.core.portfolio.domain.Portfolio;
import com.teamdears.core.portfolio.dto.PortfolioDTO;
import com.teamdears.core.portfolio.dto.PortfolioOverviewDTO;
import com.teamdears.core.portfolio.mapper.PortfolioMapper;
import com.teamdears.core.portfolio.repository.PortfolioRepository;
import com.teamdears.core.portfolio.service.PortfolioService;
import com.teamdears.core.review.service.ReviewService;
import com.teamdears.core.wishlist.domain.WishList;
import com.teamdears.core.wishlist.repository.WishListRepository;
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
    private final PortfolioRepository portfolioRepository;
    private final ReviewService reviewService;

    public List<PortfolioOverviewDTO.Response> getWishListByMember(int page, int size) {
        Long memberId = memberService.getCurrentAuthenticatedCustomer().getId();
        log.info("Fetching wishlist for member ID: {}", memberId);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<WishList> wishLists = wishListRepository.findAllByCustomerIdOrderByUpdatedAtDesc(memberId, pageRequest);
        log.info("Found {} items in wishlist for member ID: {}", wishLists.getTotalElements(), memberId);

        return wishLists.stream()
                .map(wishList -> portfolioMapper.entityToOverviewResponse(wishList.getPortfolio()))
                // set avgRating using calculateAvgRating method
                .map(portfolioOverview -> {
                    Portfolio portfolio = portfolioRepository.findById(portfolioOverview.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));
                    PortfolioDTO.Response portfolioResponse = portfolioMapper.entityToResponse(portfolio);

                    portfolioOverview.setAvgRating(calculateAvgRating(portfolioResponse));
                    portfolioOverview.setIsWishiListed(true);

                    //get review count from repository count at certain portfolio overview
                    Integer reviewCount = reviewService.getReviewCountById(portfolioResponse.getId());

                    portfolioOverview.setReviewCount(reviewCount);
                    return portfolioOverview;
                })
                .toList();
    }

    private float calculateAvgRating(PortfolioDTO.Response portfolioResponse) {
        int ratingCount = portfolioResponse.getRatingCount() != null ? portfolioResponse.getRatingCount() : 0;
        return ratingCount != 0 ? Math.round((float) portfolioResponse.getRatingSum() / ratingCount * 10) / 10f : 0;
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

    public boolean isWishListed(Long portfolioId) {
        Customer customer = memberService.getCurrentAuthenticatedCustomer();
        return wishListRepository.existsByCustomerIdAndPortfolioId(customer.getId(), portfolioId);
    }
}
