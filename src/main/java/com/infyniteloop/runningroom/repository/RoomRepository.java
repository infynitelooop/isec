package com.infyniteloop.runningroom.repository;

import com.infyniteloop.runningroom.dto.RoomResponse;
import com.infyniteloop.runningroom.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    Optional<Room> findByRoomNumber(String roomNumber);
    boolean existsByRoomNumberAndTenantId(String roomNumber, UUID tenantId);


    @Query("SELECT r FROM Room r JOIN FETCH r.building")
    List<Room> findAllWithBuilding();
}
