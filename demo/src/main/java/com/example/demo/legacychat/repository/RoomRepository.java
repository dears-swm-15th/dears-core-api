package com.example.demo.legacychat.repository;

import com.example.demo.legacychat.domain.Room;
import com.example.demo.portfolio.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Room r SET r.isDeleted = true WHERE r.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query(value = "SELECT * from Room room WHERE room.is_deleted = true", nativeQuery = true)
    List<Portfolio> findSoftDeletedRooms();

}
