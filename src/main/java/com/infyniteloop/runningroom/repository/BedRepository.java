package com.infyniteloop.runningroom.repository;

import com.infyniteloop.runningroom.model.Bed;
import com.infyniteloop.runningroom.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BedRepository extends JpaRepository<Bed, UUID> {
    Optional<Bed> findByRoomAndBedNumber(Room room, int bedNumber);
    boolean existsByBedNumberAndRoomId(int bedNumber, UUID roomId);
}