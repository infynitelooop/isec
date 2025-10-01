package com.infyniteloop.runningroom.room.controller;

import com.infyniteloop.runningroom.room.dto.RoomRequest;
import com.infyniteloop.runningroom.room.dto.RoomResponse;
import com.infyniteloop.runningroom.room.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // CREATE room
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request) {

        RoomResponse response = roomService.createRoom(request);

        // Build location URI
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response); // 201 Created
    }

    // READ all rooms
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms()); // 200 OK
    }

    // READ room by ID
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoom(@PathVariable String id) {
        return ResponseEntity.ok(roomService.getRoom(id)); // 200 OK
    }

    // UPDATE room
    @PutMapping
    public ResponseEntity<RoomResponse> updateRoom(
            @Valid @RequestBody RoomRequest request) {
        RoomResponse response = roomService.updateRoom(request);
        return ResponseEntity.ok(response); // 200 OK (resource updated)
    }

    // DELETE room
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}