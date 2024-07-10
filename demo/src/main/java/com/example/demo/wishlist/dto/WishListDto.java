package com.example.demo.wishlist.dto;

import com.example.demo.portfolio.domain.Portfolio;
import lombok.*;

import java.util.List;

public class WishListDto {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Long portfolioId;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        List<Portfolio> portfolios;
    }
}
