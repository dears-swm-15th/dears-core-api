package com.example.demo.chat.repository;

import com.example.demo.chat.domain.ReadFlag;
import com.example.demo.portfolio.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReadFlagRepository extends JpaRepository<ReadFlag, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Message r SET r.isDeleted = true WHERE r.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query(value = "SELECT * from ReadFlag r WHERE r.is_deleted = true", nativeQuery = true)
    List<Portfolio> findSoftDeletedMessages();

}