package com.example.demo.portfolio.service;

import com.example.demo.member.domain.Customer;
import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioSearchDTO;
import com.example.demo.portfolio.mapper.PortfolioMapper;
import com.example.demo.portfolio.repository.PortfolioRepository;
import com.example.demo.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PortfolioSearchService {

    private final PortfolioMapper portfolioMapper;
    private final CustomUserDetailsService memberService;
    private final WishListRepository wishListRepository;
    private final PortfolioRepository portfolioRepository;


    public List<PortfolioSearchDTO.Response> search(String keyword) {
        List<PortfolioSearchDTO.Response> resultList = new ArrayList<>();
        try {
            log.info("Searching documents with keyword: {}", keyword);

            List<Portfolio> portfolios = portfolioRepository.searchByKeyword(keyword);

            for (Portfolio portfolio : portfolios) {
                PortfolioSearchDTO.Response response = portfolioMapper.entityToSearchResponse(portfolio);
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
