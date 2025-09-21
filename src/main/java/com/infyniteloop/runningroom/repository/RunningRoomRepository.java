package com.infyniteloop.runningroom.repository;

import com.infyniteloop.runningroom.model.RunningRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RunningRoomRepository extends JpaRepository<RunningRoom, UUID> {
    Optional<RunningRoom> findByName(String name);
}