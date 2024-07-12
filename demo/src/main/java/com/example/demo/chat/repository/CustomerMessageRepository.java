package com.example.demo.chat.repository;

import com.example.demo.chat.domain.CustomerMessage;
import com.example.demo.portfolio.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CustomerMessageRepository extends JpaRepository<CustomerMessage, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE CustomerMessage c SET c.isDeleted = true WHERE c.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query(value = "SELECT * from CustomerMessage c WHERE c.is_deleted = true", nativeQuery = true)
    List<Portfolio> findSoftDeletedChatRooms();

}
