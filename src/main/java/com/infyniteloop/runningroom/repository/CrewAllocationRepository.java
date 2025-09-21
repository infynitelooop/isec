package com.infyniteloop.runningroom.repository;

import com.infyniteloop.runningroom.model.CrewAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;

public interface CrewAllocationRepository extends JpaRepository<CrewAllocation, Long> {
    List<CrewAllocation> findByCheckOutAtIsNull(); // current occupancy
    List<CrewAllocation> findByCheckOutAtBetween(Instant from, Instant to);
}