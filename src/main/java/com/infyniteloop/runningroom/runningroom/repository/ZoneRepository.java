package com.infyniteloop.runningroom.runningroom.repository;

import com.infyniteloop.runningroom.runningroom.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ZoneRepository extends JpaRepository<Zone, UUID> {
    Optional<Zone> findByName(String name);
}
