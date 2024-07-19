package com.example.demo.review.repository;

import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE Review r SET r.isDeleted = true WHERE r.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query(value = "SELECT * from Review r WHERE r.is_deleted = true", nativeQuery = true)
    List<Review> findSoftDeletedReviews();

    @Query(value = "SELECT * from Review r WHERE r.is_provided = true and r.reviewer_id = :weddingPlannerId", nativeQuery = true)
    List<Review> findReviewsForWeddingPlanner(Long weddingPlannerId);

    @Query(value = "SELECT * from Review r WHERE r.is_provided = false and r.reviewer_id = :customerId", nativeQuery = true)
    List<Review> findReviewsForCustomer(Long customerId);

}