package com.infyniteloop.runningroom.controller;

import com.infyniteloop.runningroom.dto.RoomRequest;
import com.infyniteloop.runningroom.dto.RoomResponse;
import com.infyniteloop.runningroom.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // CREATE room
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request,
                                                   @RequestHeader("X-Tenant-Id") String tenantIdHeader) {

        UUID tenantId = UUID.fromString(tenantIdHeader); // convert to UUID
        RoomResponse response = roomService.createRoom(request, tenantId);

        // Build location URI: /api/rooms/{id}
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response); // 201 Created
    }

//    // READ all rooms
//    @GetMapping
//    public ResponseEntity<List<RoomResponse>> getAllRooms() {
//        return ResponseEntity.ok(roomService.getAllRooms()); // 200 OK
//    }
//
//    // READ room by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<RoomResponse> getRoomById(@PathVariable String id) {
//        return ResponseEntity.ok(roomService.getRoomById(id)); // 200 OK
//    }
//
//    // UPDATE room
//    @PutMapping("/{id}")
//    public ResponseEntity<RoomResponse> updateRoom(
//            @PathVariable String id,
//            @Valid @RequestBody RoomRequest request) {
//        RoomResponse response = roomService.updateRoom(id, request);
//        return ResponseEntity.ok(response); // 200 OK (resource updated)
//    }
//
//    // DELETE room
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteRoom(@PathVariable String id) {
//        roomService.deleteRoom(id);
//        return ResponseEntity.noContent().build(); // 204 No Content
//    }
}