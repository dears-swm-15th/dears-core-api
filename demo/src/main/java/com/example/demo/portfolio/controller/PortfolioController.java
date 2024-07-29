package com.example.demo.portfolio.controller;

import com.example.demo.portfolio.dto.PortfolioDTO;
import com.example.demo.portfolio.dto.PortfolioSearchDTO;
import com.example.demo.portfolio.service.PortfolioSearchService;
import com.example.demo.portfolio.service.PortfolioService;
import com.example.demo.review.dto.ReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolio")
@Tag(name = "portfolio", description = "포트폴리오 API")
@Slf4j
public class PortfolioController {

    private final PortfolioService portfolioService;

    private final PortfolioSearchService portfolioSearchService;

    @GetMapping("/shared/{portfolioId}")
    @Operation(summary = "[공통] 특정 포트폴리오 조회")
    public ResponseEntity<PortfolioDTO.Response> getPortfolioById(
            @Parameter(description = "portfolioId")
            @PathVariable Long portfolioId) {
        PortfolioDTO.Response portfolioResponse = portfolioService.getPortfolioById(portfolioId);
        log.info("Fetched portfolio with ID: {}", portfolioId);
        return ResponseEntity.ok(portfolioResponse);
    }

    @GetMapping("/shared/reviews/{portfolioId}")
    @Operation(summary = "[공통] 특정 포트폴리오의 리뷰 목록 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getReviewsByPortfolioId(
            @Parameter(description = "portfolioId")
            @PathVariable Long portfolioId) {
        List<ReviewDTO.Response> reviewResponses = portfolioService.getReviewsByPortfolioId(portfolioId);
        log.info("Fetched reviews for portfolio with ID: {}", portfolioId);
        return ResponseEntity.ok(reviewResponses);
    }

    @PostMapping("/weddingplanner/create")
    @Operation(summary = "[웨딩플래너] 포트폴리오 작성")
    public ResponseEntity<PortfolioDTO.Response> createPortfolio(@RequestBody PortfolioDTO.Request portfolioRequest) {
        PortfolioDTO.Response createdPortfolio = portfolioService.createPortfolio(portfolioRequest);
        log.info("Created new portfolio with data: {}", portfolioRequest);
        return ResponseEntity.status(201).body(createdPortfolio);
    }

    @PostMapping("/weddingplanner/update/{portfolioId}")
    @Operation(summary = "[웨딩플래너] 특정 포트폴리오 업데이트")
    public ResponseEntity<PortfolioDTO.Response> updatePortfolio(
            @Parameter(description = "portfolioId")
            @PathVariable Long portfolioId, @RequestBody PortfolioDTO.Request portfolioRequest) {
        PortfolioDTO.Response updatedPortfolio = portfolioService.updatePortfolio(portfolioId, portfolioRequest);
        log.info("Updated portfolio with ID: {} with data: {}", portfolioId, portfolioRequest);
        return ResponseEntity.ok(updatedPortfolio);
    }

    @PostMapping("/weddingplanner/delete/{portfolioId}")
    @Operation(summary = "[웨딩플래너] 특정 포트폴리오 삭제")
    public ResponseEntity<Void> deletePortfolio(
            @Parameter(description = "portfolioId")
            @PathVariable Long portfolioId) {
        portfolioService.deletePortfolio(portfolioId);
        log.info("Deleted portfolio with ID: {}", portfolioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shared/soft-deleted")
    @Operation(summary = "[공통] soft-deleted가 된 포트폴리오 조회")
    public ResponseEntity<List<PortfolioDTO.Response>> getAllSoftDeleted() {
        List<PortfolioDTO.Response> softDeletedPortfolios = portfolioService.getAllSoftDeletedPortfolios();
        log.info("Fetched all soft-deleted portfolios");
        return ResponseEntity.ok(softDeletedPortfolios);
    }

    @GetMapping("/shared/all")
    @Operation(summary = "[공통] soft-deleted 를 포함한 전체 포트폴리오 조회")
    public ResponseEntity<List<PortfolioDTO.Response>> getAllPortfolios() {
        List<PortfolioDTO.Response> portfolioResponses = portfolioService.getAllPortfolios();
        log.info("Fetched all portfolios including soft-deleted ones");
        return ResponseEntity.ok(portfolioResponses);
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
}
