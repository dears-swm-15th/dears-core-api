package com.teamdears.core.wishlist.dto;

import com.teamdears.core.portfolio.domain.Portfolio;
import lombok.*;

import java.util.List;

public class WishListDto {

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
