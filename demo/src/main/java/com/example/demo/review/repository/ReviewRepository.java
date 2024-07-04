package com.example.demo.review.repository;

import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "SELECT * from Review review WHERE review.is_deleted = true", nativeQuery = true)
    List<Review> findSoftDeletedReviews();

}