package com.infyniteloop.runningroom.repository;

import com.infyniteloop.runningroom.model.RoomAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomAllocationRepository extends JpaRepository<RoomAllocation, UUID> {
}
