package com.example.demo.portfolio.service;

import com.example.demo.member.domain.Customer;
import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioSearchDTO;
import com.example.demo.portfolio.mapper.PortfolioMapper;
import com.example.demo.wishlist.repository.WishListRepository;
import com.example.demo.wishlist.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch.core.*;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
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


    public List<PortfolioSearchDTO.Response> search(String keyword) {
        List<PortfolioSearchDTO.Response> resultList = new ArrayList<>();
        try {
            log.info("Searching documents with keyword: {}", keyword);
            List<String> sourceList = List.of("services", "plannerName", "organization", "introduction");

            // TODO ====================== JPA migration =============================

//            SearchRequest request = buildSearchRequest(indexName, keyword, sourceList);
//
//            SearchResponse<PortfolioSearchDTO.Request> response = openSearchClient.search(request, PortfolioSearchDTO.Request.class);
//
//
//            List<Hit<PortfolioSearchDTO.Request>> hits = response.hits().hits();
//            for (Hit<PortfolioSearchDTO.Request> hit : hits) {
//                PortfolioSearchDTO.Response searchResponse = portfolioMapper.requestToSearchResponse(hit.source());
//                searchResponse.setIsWishListed(isWishListed(searchResponse.getId()));
//                resultList.add(searchResponse);
//            }

            // TODO ===================================================================

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
