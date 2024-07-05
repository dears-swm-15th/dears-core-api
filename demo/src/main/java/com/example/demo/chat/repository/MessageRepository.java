package com.example.demo.chat.repository;

import com.example.demo.chat.domain.Message;
import com.example.demo.portfolio.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "SELECT * from Message message WHERE room.is_deleted = true", nativeQuery = true)
    List<Portfolio> findSoftDeletedMessages();

}

