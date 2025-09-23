package com.infyniteloop.runningroom.controller;

import com.infyniteloop.runningroom.dto.BuildingRequest;
import com.infyniteloop.runningroom.dto.BuildingResponse;
import com.infyniteloop.runningroom.service.impl.BuildingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingServiceImpl buildingService;

    @GetMapping
    public ResponseEntity<List<BuildingResponse>> getAllBuildings() {
        return ResponseEntity.ok(buildingService.getAllBuildings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuildingResponse> getBuilding(@PathVariable UUID id) {
        return ResponseEntity.ok(buildingService.getBuildingById(id));
    }

    @PostMapping
    public ResponseEntity<BuildingResponse> createBuilding(@RequestBody BuildingRequest request) {
        return ResponseEntity.ok(buildingService.createBuilding(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuildingResponse> updateBuilding(@PathVariable UUID id,
                                                           @RequestBody BuildingRequest request) {
        return ResponseEntity.ok(buildingService.updateBuilding(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable UUID id) {
        buildingService.deleteBuilding(id);
        return ResponseEntity.noContent().build();
    }
}
