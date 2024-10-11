package com.example.demo.portfolio.controller;

import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.portfolio.dto.PortfolioOverviewDTO;
import com.example.demo.portfolio.dto.PortfolioSearchDTO;
import com.example.demo.portfolio.service.PortfolioSearchService;
import com.example.demo.portfolio.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discovery")
@Tag(name = "discovery", description = "탐색 API")
@Slf4j
public class DiscoveryController {
    private final PortfolioService portfolioService;

    private final PortfolioSearchService portfolioSearchService;

    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/shared/top5")
    @Operation(summary = "[공통] 조회수 상위 5개 포트폴리오 조회")
    public ResponseEntity<List<PortfolioOverviewDTO.Response>> getTop5Portfolios() {
        List<PortfolioOverviewDTO.Response> top5Portfolios = portfolioService.getTop5Portfolios();
        return ResponseEntity.ok(top5Portfolios);
    }

    @GetMapping("/shared/search")
    @Operation(summary = "[공통] 포트폴리오 검색하여 조회")
    public ResponseEntity<List<PortfolioSearchDTO.Response>> getSearchPortfolio(
            @Parameter(description = "검색 키워드")
            @RequestParam String content) {
        List<PortfolioSearchDTO.Response> searchResult = portfolioSearchService.search(content);
        log.info("Searched portfolios with content: {}", content);
        return ResponseEntity.ok(searchResult);
    }

    @GetMapping("/shared/recommend")
    @Operation(summary = "[공통] 추천 포트폴리오 조회")
    public ResponseEntity<List<PortfolioOverviewDTO.Response>> getRecommendPortfolios() {
        List<PortfolioOverviewDTO.Response> recommendPortfolios = portfolioService.getRecommendPortfolios();
        return ResponseEntity.ok(recommendPortfolios);
    }
}
