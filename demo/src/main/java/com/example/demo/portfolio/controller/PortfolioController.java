package com.example.demo.portfolio.controller;

import com.example.demo.portfolio.service.PortfolioService;
import com.example.demo.portfolio.dto.PortfolioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolios")
@Tag(name = "portfolio", description = "포트폴리오 API")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("")
    @Operation(summary = "전체 포트폴리오 조회")
    public ResponseEntity<List<PortfolioDTO.Response>> getAllPortfolios() {
        List<PortfolioDTO.Response> portfolios = portfolioService.getAllPortfolios();
        return ResponseEntity.ok(portfolios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 포트폴리오 조회")
    public ResponseEntity<PortfolioDTO.Response> getPortfolioById(@PathVariable Long id) {
        Optional<PortfolioDTO.Response> portfolio = portfolioService.getPortfolioById(id);
        return portfolio.map(ResponseEntity::ok).orElseThrow(NoSuchElementException::new);
    }

    @PostMapping("")
    @Operation(summary = "포트폴리오 작성")
    @Parameters({
        @Parameter(name = "organization", example = "업체명", schema = @Schema(type = "string"), required = true),
        @Parameter(name = "region", example = "활동 지역", schema = @Schema(type = "string"),  required = true),
        @Parameter(name = "introduction", example = "플래너 한 줄 소개", schema = @Schema(type = "string"),  required = true),
        @Parameter(name = "officeHours", example = "근무 시간", schema = @Schema(type = "string"),  required = true),
        @Parameter(name = "contactInfo", example = "연락처", schema = @Schema(type = "string"),  required = true),
        @Parameter(name = "image", example = "플래너 프로필 사진 경로", schema = @Schema(type = "string"),  required = true),
        @Parameter(name = "consultationFee", example = "상담 비용", schema = @Schema(type = "integer"),  required = true),
        @Parameter(name = "description", example = "플래너 세부 소개", schema = @Schema(type = "string"),  required = true),
        @Parameter(name = "weddingPhotos", example = "웨딩 사진 경로", schema = @Schema(type = "string"),  required = true)
    })
    public ResponseEntity<PortfolioDTO.Response> createPortfolio(@RequestBody PortfolioDTO.Request portfolioRequest) {
        PortfolioDTO.Response createdPortfolio = portfolioService.createPortfolio(portfolioRequest);
        return ResponseEntity.status(201).body(createdPortfolio);
    }

    @PutMapping("/{id}")
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

    @ExceptionHandler(NoSuchElementException.class)
    @ApiResponse(responseCode = "404", description = "조회된 포트폴리오 없음")
    protected ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Portfolio not found");
    }

    @ExceptionHandler(RuntimeException.class)
    @ApiResponse(responseCode = "500", description = "서버 에러")
    protected ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
