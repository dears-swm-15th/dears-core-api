package com.example.demo.wishlist.controller;

import com.example.demo.portfolio.dto.PortfolioOverviewDTO;
import com.example.demo.wishlist.dto.WishListDto;
import com.example.demo.wishlist.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishlist")
@Tag(name = "wishlist", description = "wishlist API")
public class WishListController {

    private final WishListService wishListService;

    @GetMapping("")
    @Operation(summary = "위시리스트 목록 조회", description = "자신의 위시리스트 목록을 조회합니다.")
    public ResponseEntity<List<PortfolioOverviewDTO.Response>> getWishListByMember(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                            @RequestParam(value = "size", defaultValue = "10") int size){
        List<PortfolioOverviewDTO.Response> portfolioOverviews = wishListService.getWishListByMember(page, size);
        return ResponseEntity.ok(portfolioOverviews);
    }

    @PostMapping("")
    @Operation(summary = "위시리스트 추가", description = "자신이 원하는 포트폴리오를 위시리스트에 추가합니다.")
    public ResponseEntity<Void> addWishList(@RequestBody WishListDto.Request request){
        wishListService.addWishList(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    @Operation(summary = "위시리스트 삭제", description = "자신의 위시리스트에서 포트폴리오를 삭제합니다.")
    public ResponseEntity<Void> deleteWishList(@RequestBody WishListDto.Request request){
        wishListService.deleteWishList(request);
        return ResponseEntity.ok().build();
    }
}
