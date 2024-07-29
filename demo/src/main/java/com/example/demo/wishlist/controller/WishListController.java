package com.example.demo.wishlist.controller;

import com.example.demo.portfolio.dto.PortfolioOverviewDTO;
import com.example.demo.wishlist.service.WishListService;
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
@Slf4j
@RequestMapping("/api/v1/wishlist")
@Tag(name = "wishlist", description = "wishlist API")
public class WishListController {

    private final WishListService wishListService;

    @GetMapping("/customer/me")
    @Operation(summary = "[신랑신부] 위시리스트 목록 조회", description = "customer가 자신의 위시리스트 목록을 조회합니다.")
    public ResponseEntity<List<PortfolioOverviewDTO.Response>> getWishListByMember(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        List<PortfolioOverviewDTO.Response> portfolioOverviews = wishListService.getWishListByMember(page, size);
        log.info("Fetching wishlist for customer with page: {} and size: {}", page, size);
        return ResponseEntity.ok(portfolioOverviews);
    }

    @PostMapping("/customer/post/{portfolioId}")
    @Operation(summary = "[신랑신부] 위시리스트 추가", description = "customer가 자신이 원하는 포트폴리오를 위시리스트에 추가합니다.")
    public ResponseEntity<Void> addWishList(
            @Parameter(description = "portfolioId")
            @PathVariable("portfolioId") Long portfolioId) {
        wishListService.addWishList(portfolioId);
        log.info("Adding portfolio with ID: {} to wishlist", portfolioId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/customer/delete/{portfolioId}")
    @Operation(summary = "[신랑신부] 위시리스트 삭제", description = "customer가 자신의 위시리스트에서 포트폴리오를 삭제합니다.")
    public ResponseEntity<Void> deleteWishList(
            @Parameter(description = "portfolioId")
            @PathVariable("portfolioId") Long portfolioId) {
        wishListService.deleteWishList(portfolioId);
        log.info("Deleting portfolio with ID: {} from wishlist", portfolioId);
        return ResponseEntity.ok().build();
    }
}
