package com.infyniteloop.runningroom.repository;

import com.infyniteloop.runningroom.model.Bed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BedRepository extends JpaRepository<Bed, UUID> {
    Optional<Bed> findByBedNumber(String bedNumber);
    boolean existsByBedNumberAndRoomId(String bedNumber, UUID roomId);
}