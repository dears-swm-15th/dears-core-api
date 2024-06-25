package com.example.demo.portfolio.controller;

import com.example.demo.portfolio.service.PortfolioService;
import com.example.demo.portfolio.dto.PortfolioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("")
    public ResponseEntity<List<PortfolioDTO.Response>> getAllPortfolios() {
        List<PortfolioDTO.Response> portfolios = portfolioService.getAllPortfolios();
        return ResponseEntity.ok(portfolios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioDTO.Response> getPortfolioById(@PathVariable Long id) {
        Optional<PortfolioDTO.Response> portfolio = portfolioService.getPortfolioById(id);
        return portfolio.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<PortfolioDTO.Response> createPortfolio(@RequestBody PortfolioDTO.Request portfolioRequest) {
        PortfolioDTO.Response createdPortfolio = portfolioService.createPortfolio(portfolioRequest);
        return ResponseEntity.status(201).body(createdPortfolio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PortfolioDTO.Response> updatePortfolio(@PathVariable Long id, @RequestBody PortfolioDTO.Request portfolioRequest) {
        try {
            PortfolioDTO.Response updatedPortfolio = portfolioService.updatePortfolio(id, portfolioRequest);
            return ResponseEntity.ok(updatedPortfolio);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id) {
        try {
            portfolioService.deletePortfolio(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
