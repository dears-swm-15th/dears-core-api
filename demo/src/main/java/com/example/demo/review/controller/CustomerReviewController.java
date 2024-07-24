package com.example.demo.review.controller;

import com.example.demo.review.dto.ReviewDTO;
import com.example.demo.review.service.CustomerReviewServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/review")
@Tag(name = "review", description = "신랑신부 리뷰 API")
public class CustomerReviewController {

    private final CustomerReviewServiceImpl customerReviewService;

    @PostMapping("/create")
    @Operation(summary = "[신랑신부] 리뷰 작성")
    public ResponseEntity<ReviewDTO.Response> createReviewForCustomer(@RequestBody ReviewDTO.Request reviewRequest) {
        ReviewDTO.Response createdReview = customerReviewService.createReview(reviewRequest);
        return ResponseEntity.ok(createdReview);
    }

    @PostMapping("/update/{id}")
    @Operation(summary = "[신랑신부] 특정 리뷰 업데이트")
    public ResponseEntity<ReviewDTO.Response> updateReviewForCustomer(
            @Parameter(description = "reviewId")
            @PathVariable Long id, @RequestBody ReviewDTO.Request reviewRequest) {
        ReviewDTO.Response updatedReview = customerReviewService.modifyReview(id, reviewRequest);
        return ResponseEntity.ok(updatedReview);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "[신랑신부] 특정 리뷰 삭제")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "reviewId")
            @PathVariable Long id) {
        customerReviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get")
    @Operation(summary = "[신랑신부] 내 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getMyReviewsForCustomer() {
        List<ReviewDTO.Response> myReviews = customerReviewService.getMyReviews();
        return ResponseEntity.ok(myReviews);
    }

}
