package com.example.demo.review.controller;

import com.example.demo.review.dto.ReviewDTO;
import com.example.demo.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
@Tag(name = "review", description = "리뷰 API")
public class ReviewCotroller {

    private final ReviewService reviewService;

    @GetMapping("/shared/all")
    @Operation(summary = "[공통] 전체 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getAllReviews() {
        List<ReviewDTO.Response> reviewResponses = reviewService.getAllReviews();
        return ResponseEntity.ok(reviewResponses);
    }

    @GetMapping("/shared/{id}")
    @Operation(summary = "[공통] 특정 리뷰 조회")
    public ResponseEntity<ReviewDTO.Response> getReviewById(
            @Parameter(description = "reviewId")
            @PathVariable Long id) {
        ReviewDTO.Response reviewResponse = reviewService.getReviewById(id);
        return ResponseEntity.ok(reviewResponse);
    }
    @PostMapping("/weddingplanner/create")
    @Operation(summary = "[웨딩플래너] 리뷰 작성")
    public ResponseEntity<ReviewDTO.Response> createReviewForWeddingPlanner(@RequestBody ReviewDTO.Request reviewRequest) {
        ReviewDTO.Response createdReview = reviewService.createReviewForWeddingPlanner(reviewRequest);
        return ResponseEntity.ok(createdReview);
    }

    @PostMapping("/customer/create")
    @Operation(summary = "[신랑신부] 리뷰 작성")
    public ResponseEntity<ReviewDTO.Response> createReviewForCustomer(@RequestBody ReviewDTO.Request reviewRequest) {
        ReviewDTO.Response createdReview = reviewService.createReviewForCustomer(reviewRequest);
        return ResponseEntity.ok(createdReview);
    }

    @PostMapping("/weddingplanner/update/{id}")
    @Operation(summary = "[웨딩플래너] 특정 리뷰 업데이트")
    public ResponseEntity<ReviewDTO.Response> updateReviewForWeddingPlanner(
            @Parameter(description = "reviewId")
            @PathVariable Long id, @RequestBody ReviewDTO.Request reviewRequest) {
        ReviewDTO.Response updatedReview = reviewService.modifyReviewForWeddingPlanner(id, reviewRequest);
        return ResponseEntity.ok(updatedReview);
    }

    @PostMapping("/customer/update/{id}")
    @Operation(summary = "[신랑신부] 특정 리뷰 업데이트")
    public ResponseEntity<ReviewDTO.Response> updateReviewForCustomer(
            @Parameter(description = "reviewId")
            @PathVariable Long id, @RequestBody ReviewDTO.Request reviewRequest) {
        ReviewDTO.Response updatedReview = reviewService.modifyReviewForCustomer(id, reviewRequest);
        return ResponseEntity.ok(updatedReview);
    }

    @PostMapping("/weddingplanner/delete/{id}")
    @Operation(summary = "[웨딩플래너] 특정 리뷰 삭제")
    public ResponseEntity<Void> deleteReviewForWeddingPlanner(
            @Parameter(description = "reviewId")
            @PathVariable Long id) {
        reviewService.deleteReviewForWeddingPlanner(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/customer/delete/{id}")
    @Operation(summary = "[신랑신부] 특정 리뷰 삭제")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "reviewId")
            @PathVariable Long id) {
        reviewService.deleteReviewForCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shared/soft-deleted")
    @Operation(summary = "[공통] soft-deleted 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getAllSoftDeleted() {
        List<ReviewDTO.Response> softDeletedReviews = reviewService.getAllSoftDeletedReviews();
        return ResponseEntity.ok(softDeletedReviews);
    }

    @GetMapping("/customer/me")
    @Operation(summary = "[신랑신부] 내 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getMyReviewsForCustomer() {
        List<ReviewDTO.Response> myReviews = reviewService.getMyReviewsForCustomer();
        return ResponseEntity.ok(myReviews);
    }

    @GetMapping("/weddingplanner/me")
    @Operation(summary = "[웨딩플래너] 내 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getMyReviewsForWeddingplanner() {

        List<ReviewDTO.Response> myReviews = reviewService.getMyReviewsForWeddingplanner();
        return ResponseEntity.ok(myReviews);
    }
}
