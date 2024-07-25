package com.example.demo.portfolio.controller;

import com.example.demo.portfolio.dto.PortfolioDTO;
import com.example.demo.portfolio.dto.PortfolioSearchDTO;
import com.example.demo.portfolio.service.PortfolioSearchService;
import com.example.demo.portfolio.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolio")
@Tag(name = "portfolio", description = "포트폴리오 API")
public class PortfolioController {

    private final PortfolioService portfolioService;

    private final PortfolioSearchService portfolioSearchService;

    @GetMapping("/shared/{id}")
    @Operation(summary = "[공통] 특정 포트폴리오 조회")
    public ResponseEntity<PortfolioDTO.Response> getPortfolioById(
            @Parameter(description = "portfolioId")
            @PathVariable Long id) {
        PortfolioDTO.Response portfolioResponse = portfolioService.getPortfolioById(id);
        return ResponseEntity.ok(portfolioResponse);
    }

    @PostMapping("/weddingplanner/create")
    @Operation(summary = "[웨딩플래너] 포트폴리오 작성")
    public ResponseEntity<PortfolioDTO.Response> createPortfolio(@RequestBody PortfolioDTO.Request portfolioRequest) {
        PortfolioDTO.Response createdPortfolio = portfolioService.createPortfolio(portfolioRequest);
        return ResponseEntity.status(201).body(createdPortfolio);
    }

    @PostMapping("/weddingplanner/update/{id}")
    @Operation(summary = "[웨딩플래너] 특정 포트폴리오 업데이트")
    public ResponseEntity<PortfolioDTO.Response> updatePortfolio(
            @Parameter(description = "portfolioId")
            @PathVariable Long id, @RequestBody PortfolioDTO.Request portfolioRequest) {
        PortfolioDTO.Response updatedPortfolio = portfolioService.updatePortfolio(id, portfolioRequest);
        return ResponseEntity.ok(updatedPortfolio);
    }

    @PostMapping("/weddingplanner/delete/{id}")
    @Operation(summary = "[웨딩플래너] 특정 포트폴리오 삭제")
    public ResponseEntity<Void> deletePortfolio(
            @Parameter(description = "portfolioId")
            @PathVariable Long id) {
        portfolioService.deletePortfolio(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shared/soft-deleted")
    @Operation(summary = "[공통] soft-deleted가 된 포트폴리오 조회")
    public ResponseEntity<List<PortfolioDTO.Response>> getAllSoftDeleted() {
        List<PortfolioDTO.Response> softDeletedPortfolios = portfolioService.getAllSoftDeletedPortfolios();
        return ResponseEntity.ok(softDeletedPortfolios);
    }

    @GetMapping("/shared/all")
    @Operation(summary = "[공통] soft-deleted 를 포함한 전체 포트폴리오 조회")
    public ResponseEntity<List<PortfolioDTO.Response>> getAllPortfolios() {
        List<PortfolioDTO.Response> portfolioResponses = portfolioService.getAllPortfolios();
        return ResponseEntity.ok(portfolioResponses);
    }

    @GetMapping("/shared/search")
    @Operation(summary = "[공통] 포트폴리오 검색하여 조회")
    public ResponseEntity<List<PortfolioSearchDTO.Response>> getSearchPortfolio(
            @Parameter(description = "검색 키워드")
            @RequestParam String content) {
        List<PortfolioSearchDTO.Response> searchResult = portfolioSearchService.search(content);
        return ResponseEntity.ok(searchResult);
    }

}
