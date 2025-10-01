package com.infyniteloop.runningroom.building.service;

import com.infyniteloop.runningroom.building.dto.BuildingRequest;
import com.infyniteloop.runningroom.building.dto.BuildingResponse;

import java.util.List;
import java.util.UUID;

public interface BuildingService {
    List<BuildingResponse> getAllBuildings();

    BuildingResponse getBuildingById(UUID id);

    BuildingResponse createBuilding(BuildingRequest request);

    BuildingResponse updateBuilding(UUID id, BuildingRequest request);

    void deleteBuilding(UUID id);
}
