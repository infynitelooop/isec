package com.infyniteloop.runningroom.runningroom.controller;


import com.infyniteloop.runningroom.runningroom.entity.RunningRoom;
import com.infyniteloop.runningroom.runningroom.repository.RunningRoomRepository;
import com.infyniteloop.runningroom.runningroom.service.RunningRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/runningrooms")
@RequiredArgsConstructor
public class RunningRoomController {

    private final RunningRoomService runningRoomService;


    // Implement all endpoints for CRUD operations on RunningRoom entity

    @GetMapping
    public List<RunningRoom> findAll(){
        return runningRoomService.findAll();
    }

    @GetMapping("/id")
    public RunningRoom findById(UUID id){
        return runningRoomService.findById(id).orElseThrow(() -> new RuntimeException("Running Room not found"));
    }

    @PostMapping
    public ResponseEntity<RunningRoom> save(@RequestBody RunningRoom runningRoom){
        return ResponseEntity.ok(runningRoomService.createRunningroom(runningRoom));
    }

}
