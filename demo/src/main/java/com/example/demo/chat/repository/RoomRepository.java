package com.example.demo.chat.repository;

import com.example.demo.chat.domain.Room;
import com.example.demo.portfolio.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(value = "SELECT * from Room room WHERE room.is_deleted = true", nativeQuery = true)
    List<Portfolio> findSoftDeletedRooms();

}
