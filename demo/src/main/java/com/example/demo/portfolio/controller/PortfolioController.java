package com.example.demo.portfolio.controller;

import com.example.demo.portfolio.service.PortfolioService;
import com.example.demo.portfolio.dto.PortfolioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
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
        return portfolio.map(ResponseEntity::ok).orElseThrow(NoSuchElementException::new);
    }

    @PostMapping("")
    public ResponseEntity<PortfolioDTO.Response> createPortfolio(@RequestBody PortfolioDTO.Request portfolioRequest) {
        PortfolioDTO.Response createdPortfolio = portfolioService.createPortfolio(portfolioRequest);
        return ResponseEntity.status(201).body(createdPortfolio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PortfolioDTO.Response> updatePortfolio(@PathVariable Long id, @RequestBody PortfolioDTO.Request portfolioRequest) {
        PortfolioDTO.Response updatedPortfolio = portfolioService.updatePortfolio(id, portfolioRequest);
        return ResponseEntity.ok(updatedPortfolio);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id) {
        portfolioService.deletePortfolio(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Portfolio not found");
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
