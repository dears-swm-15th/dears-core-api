package com.example.demo.portfolio.repository;

import com.example.demo.portfolio.domain.Portfolio;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Portfolio findByWeddingPlannerId(Long weddingPlannerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Portfolio p where p.id = :id")
    Optional<Portfolio> findPortfolioByIdWithPessimisticLock(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Portfolio p SET p.isDeleted = true WHERE p.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query(value = "SELECT * from Portfolio portfolio WHERE portfolio.is_deleted = true", nativeQuery = true)
    List<Portfolio> findSoftDeletedPortfolios();
}
