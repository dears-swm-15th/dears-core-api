package com.teamdears.core.portfolio.controller;

import com.teamdears.core.member.service.CustomUserDetailsService;
import com.teamdears.core.portfolio.dto.PortfolioDTO;
import com.teamdears.core.portfolio.service.PortfolioSearchService;
import com.teamdears.core.portfolio.service.PortfolioService;
import com.teamdears.core.review.dto.ReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.teamdears.core.enums.member.MemberRole.CUSTOMER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolio")
@Tag(name = "portfolio", description = "포트폴리오 API")
@Slf4j
public class PortfolioController {

    private final PortfolioService portfolioService;

    private final PortfolioSearchService portfolioSearchService;

    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/shared/{portfolioId}")
    @Operation(summary = "[공통] 특정 포트폴리오 조회")
    public ResponseEntity<PortfolioDTO.Response> getPortfolioById(
            @Parameter(description = "portfolioId")
            @PathVariable Long portfolioId) {
        if (customUserDetailsService.getCurrentAuthenticatedMemberRole().equals(CUSTOMER)) {
            portfolioService.increaseViewCount(portfolioId);
        }
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
        return ResponseEntity.ok().build();
    }

    @GetMapping("/shared/all")
    @Operation(summary = "[공통] soft-deleted 를 포함한 전체 포트폴리오 조회")
    public ResponseEntity<List<PortfolioDTO.Response>> getAllPortfolios() {
        List<PortfolioDTO.Response> portfolioResponses = portfolioService.getAllPortfolios();
        log.info("Fetched all portfolios including soft-deleted ones");
        return ResponseEntity.ok(portfolioResponses);
    }


    @GetMapping("/weddingplanner/me")
    @Operation(summary = "[웨딩플래너] 내 포트폴리오 조회")
    public ResponseEntity<PortfolioDTO.Response> getMyPortfolio() {
        PortfolioDTO.Response myPortfolio = portfolioService.getMyPortfolio();
        return ResponseEntity.ok(myPortfolio);
    }
}
