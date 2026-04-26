package com.infyniteloop.runningroom.runningroom.service;

import com.infyniteloop.runningroom.runningroom.entity.RunningRoom;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RunningRoomService {
    RunningRoom createRunningroom(RunningRoom request);

    RunningRoom findByName(String name);

    Optional<RunningRoom> findById(UUID id);

    List<RunningRoom> findAll();

    // TODO: May not be provided as part of the initial implementation, but we can add it later if needed
    //RunningRoom updateRunningroom(UUID id, RunningRoom request);
}
