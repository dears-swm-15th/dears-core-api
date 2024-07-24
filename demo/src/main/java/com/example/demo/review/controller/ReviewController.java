package com.example.demo.review.controller;

import com.example.demo.review.dto.ReviewDTO;
import com.example.demo.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "review", description = "공통 리뷰 API")
public class ReviewController {

    private final ReviewService reviewService;


    @GetMapping("/review/get")
    @Operation(summary = "[공통] 전체 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getAllReviews() {
        List<ReviewDTO.Response> reviewResponses = reviewService.getAllReviews();
        return ResponseEntity.ok(reviewResponses);
    }

    @GetMapping("/review/get/{id}")
    @Operation(summary = "[공통] 특정 리뷰 조회")
    public ResponseEntity<ReviewDTO.Response> getReviewById(
            @Parameter(description = "reviewId")
            @PathVariable Long id) {
        ReviewDTO.Response reviewResponse = reviewService.getReviewById(id);
        return ResponseEntity.ok(reviewResponse);
    }

    @GetMapping("/review/get/soft-deleted")
    @Operation(summary = "[공통] soft-deleted 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getAllSoftDeleted() {
        List<ReviewDTO.Response> softDeletedReviews = reviewService.getAllSoftDeletedReviews();
        return ResponseEntity.ok(softDeletedReviews);
    }

}
