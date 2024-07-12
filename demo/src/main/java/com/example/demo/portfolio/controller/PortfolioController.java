package com.example.demo.portfolio.controller;

import com.example.demo.portfolio.dto.PortfolioDTO;
import com.example.demo.portfolio.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
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

    @GetMapping("")
    @Operation(summary = "전체 포트폴리오 조회")
    public ResponseEntity<List<PortfolioDTO.Response>> getAllPortfolios() {
        List<PortfolioDTO.Response> portfolioResponses = portfolioService.getAllPortfolios();
        return ResponseEntity.ok(portfolioResponses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 포트폴리오 조회")
    public ResponseEntity<PortfolioDTO.Response> getPortfolioById(@PathVariable Long id) {
        PortfolioDTO.Response portfolioResponse = portfolioService.getPortfolioById(id);
        return ResponseEntity.ok(portfolioResponse);
    }

    @PostMapping("")
    @Operation(summary = "포트폴리오 작성")
    public ResponseEntity<PortfolioDTO.Response> createPortfolio(@RequestBody PortfolioDTO.Request portfolioRequest) {
        PortfolioDTO.Response createdPortfolio = portfolioService.createPortfolio(portfolioRequest);
        return ResponseEntity.status(201).body(createdPortfolio);
    }

    @PostMapping("/update/{id}")
    @Operation(summary = "특정 포트폴리오 업데이트")
    public ResponseEntity<PortfolioDTO.Response> updatePortfolio(@PathVariable Long id, @RequestBody PortfolioDTO.Request portfolioRequest) {
        PortfolioDTO.Response updatedPortfolio = portfolioService.updatePortfolio(id, portfolioRequest);
        return ResponseEntity.ok(updatedPortfolio);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "특정 포트폴리오 삭제")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id) {
        portfolioService.deletePortfolio(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/soft-deleted")
    @Operation(summary = "soft-deleted 포트폴리오 조회")
    public ResponseEntity<List<PortfolioDTO.Response>> getAllSoftDeleted() {
        List<PortfolioDTO.Response> softDeletedPortfolios = portfolioService.getAllSoftDeletedPortfolios();
        return ResponseEntity.ok(softDeletedPortfolios);
    }

}
