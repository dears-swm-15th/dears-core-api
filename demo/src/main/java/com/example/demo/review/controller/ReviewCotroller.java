package com.example.demo.review.controller;

import com.example.demo.review.dto.ReviewDTO;
import com.example.demo.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
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

    @GetMapping("")
    @Operation(summary = "전체 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getAllReviews() {
        List<ReviewDTO.Response> reviewResponses = reviewService.getAllReviews();
        return ResponseEntity.ok(reviewResponses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 리뷰 조회")
    public ResponseEntity<ReviewDTO.Response> getReviewById(@PathVariable Long id) {
        ReviewDTO.Response reviewResponse = reviewService.getReviewById(id);
        return ResponseEntity.ok(reviewResponse);
    }
    @PostMapping("")
    @Operation(summary = "리뷰 작성")
    public ResponseEntity<ReviewDTO.Response> createReview(@RequestBody ReviewDTO.Request reviewRequest) {
        ReviewDTO.Response createdReview = reviewService.createReview(reviewRequest);
        return ResponseEntity.ok(createdReview);
    }

    @PostMapping("update/{id}")
    @Operation(summary = "특정 리뷰 업데이트")
    public ResponseEntity<ReviewDTO.Response> updateReview(@PathVariable Long id, @RequestBody ReviewDTO.Request reviewRequest) {
        ReviewDTO.Response updatedReview = reviewService.modifyReview(id, reviewRequest);
        return ResponseEntity.ok(updatedReview);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "특정 리뷰 삭제")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/soft-deleted")
    @Operation(summary = "soft-deleted 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getAllSoftDeleted() {
        List<ReviewDTO.Response> softDeletedReviews = reviewService.getAllSoftDeletedReviews();
        return ResponseEntity.ok(softDeletedReviews);
    }

    @GetMapping("/customer/myreview")
    @Operation(summary = "[예비신랑신부용] 내 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getMyReviewsForCustomer() {
        List<ReviewDTO.Response> myReviews = reviewService.getMyReviewsForCustomer();
        return ResponseEntity.ok(myReviews);
    }

    @GetMapping("/weddingplanner/myreview")
    @Operation(summary = "[웨딩플래너용] 내 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getMyReviewsForWeddingplanner() {

        List<ReviewDTO.Response> myReviews = reviewService.getMyReviewsForWeddingplanner();
        return ResponseEntity.ok(myReviews);
    }
}
