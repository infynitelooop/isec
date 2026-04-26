package com.infyniteloop.runningroom.runningroom.repository;

import com.infyniteloop.runningroom.runningroom.entity.RunningRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RunningRoomRepository extends JpaRepository<RunningRoom, UUID> {
    Optional<RunningRoom> findByName(String name);
    boolean existsByName(String name);
}