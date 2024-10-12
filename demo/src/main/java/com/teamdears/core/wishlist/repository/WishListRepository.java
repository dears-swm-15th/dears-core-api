package com.teamdears.core.wishlist.repository;

import com.teamdears.core.wishlist.domain.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

    Page<WishList> findAllByCustomerId(Long customerId, Pageable pageable);

    @Query("SELECT COUNT(w) FROM WishList w WHERE w.portfolio = :portfolioId")
    long countByPortfolioId(@Param("portfolioId") Long portfolioId);

    void deleteByCustomerIdAndPortfolioId(Long customerId, Long portfolioId);

    boolean existsByCustomerIdAndPortfolioId(Long customerId, Long portfolioId);

    Page<WishList> findAllByCustomerIdOrderByUpdatedAtDesc(Long customerId, Pageable pageable);
}
