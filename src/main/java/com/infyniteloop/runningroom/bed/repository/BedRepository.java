package com.infyniteloop.runningroom.bed.repository;

import com.infyniteloop.runningroom.bed.entity.Bed;
import com.infyniteloop.runningroom.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BedRepository extends JpaRepository<Bed, UUID> {
    Optional<Bed> findByRoomAndBedNumber(Room room, int bedNumber);
    boolean existsByBedNumberAndRoomId(int bedNumber, UUID roomId);
}