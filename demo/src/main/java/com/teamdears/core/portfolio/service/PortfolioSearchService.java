package com.teamdears.core.portfolio.service;

import com.teamdears.core.member.domain.Customer;
import com.teamdears.core.member.service.CustomUserDetailsService;
import com.teamdears.core.portfolio.domain.Portfolio;
import com.teamdears.core.portfolio.dto.PortfolioOverviewDTO;
import com.teamdears.core.portfolio.mapper.PortfolioMapper;
import com.teamdears.core.portfolio.repository.PortfolioRepository;
import com.teamdears.core.wishlist.repository.WishListRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PortfolioSearchService {

    private final PortfolioMapper portfolioMapper;
    private final CustomUserDetailsService memberService;
    private final WishListRepository wishListRepository;
    private final PortfolioRepository portfolioRepository;


    public List<PortfolioOverviewDTO.Response> search(String keyword) {
        List<PortfolioOverviewDTO.Response> resultList = new ArrayList<>();
        try {
            log.info("Searching documents with keyword: {}", keyword);

            List<Portfolio> portfolios = portfolioRepository.searchByKeyword(keyword);

            for (Portfolio portfolio : portfolios) {
                PortfolioOverviewDTO.Response response = portfolioMapper.entityToOverviewResponse(portfolio);
                response.setIsWishListed(isWishListed(portfolio.getId()));
                resultList.add(response);
            }

            log.info("Found {} documents with keyword: {}", resultList.size(), keyword);
        } catch (Exception e) {
            log.error("Error searching documents with keyword: {}", keyword, e);
        }
        return resultList;
    }

    public boolean isWishListed(Long portfolioId) {
        Customer customer = memberService.getCurrentAuthenticatedCustomer();
        return wishListRepository.existsByCustomerIdAndPortfolioId(customer.getId(), portfolioId);
    }
}
