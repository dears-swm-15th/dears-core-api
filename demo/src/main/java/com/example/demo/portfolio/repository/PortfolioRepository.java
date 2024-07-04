package com.example.demo.portfolio.repository;

import com.example.demo.portfolio.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    @Query(value = "SELECT * from Portfolio portfolio WHERE portfolio.is_deleted = true", nativeQuery = true)
    List<Portfolio> findSoftDeletedPortfolios();

}
