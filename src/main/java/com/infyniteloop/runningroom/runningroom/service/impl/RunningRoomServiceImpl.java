package com.infyniteloop.runningroom.runningroom.service.impl;

import com.infyniteloop.runningroom.exception.DuplicateResourceException;
import com.infyniteloop.runningroom.runningroom.entity.RunningRoom;
import com.infyniteloop.runningroom.runningroom.repository.RunningRoomRepository;
import com.infyniteloop.runningroom.runningroom.service.RunningRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RunningRoomServiceImpl implements RunningRoomService {

    private static final Logger log = LoggerFactory.getLogger(RunningRoomServiceImpl.class);
    private final RunningRoomRepository runningRoomRepository;

    public RunningRoomServiceImpl(RunningRoomRepository runningRoomRepository) {
        this.runningRoomRepository = runningRoomRepository;
    }

    @Override
    public RunningRoom findByName(String name){
        return runningRoomRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Running Room not found"));
    }

    @Override
    public Optional<RunningRoom> findById(UUID id){
        return runningRoomRepository.findById(id);
    }

    @Override
    public List<RunningRoom> findAll(){
        return runningRoomRepository.findAll();
    }

    @Override
    public RunningRoom createRunningroom(RunningRoom request) {

        //check before saving to provide a friendly error instead of waiting for the DB exception
        if (runningRoomRepository.existsByName(request.getName())) {
            log.info("Running Room {} already exists", request.getName());
            throw new DuplicateResourceException("Running Room " + request.getName() + " already exists");
        }

        RunningRoom runningRoom = new RunningRoom();
        runningRoom.setName(request.getName());
        runningRoom.setDescription(request.getDescription());
        runningRoom.setDivision(request.getDivision());
        runningRoom.setSubsidisedMeal(request.getSubsidisedMeal());
        return runningRoomRepository.save(runningRoom);
    }

// TODO: May not be provided as part of the initial implementation, but we can add it later if needed

//    @Override
//    public RunningRoom updateRunningroom(UUID id, RunningRoom request) {
//        RunningRoom existingRunningRoom = runningRoomRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Running Room not found"));
//
//        existingRunningRoom.setName(request.getName());
//        existingRunningRoom.setDescription(request.getDescription());
//        existingRunningRoom.setDivision(request.getDivision());
//        existingRunningRoom.setZone(request.getZone());
//        existingRunningRoom.setSubsidisedMeal(request.getSubsidisedMeal());
//
//        return runningRoomRepository.save(existingRunningRoom);
//    }
}
