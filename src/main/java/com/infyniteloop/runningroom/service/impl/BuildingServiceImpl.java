package com.infyniteloop.runningroom.service.impl;

import com.infyniteloop.runningroom.dto.BuildingRequest;
import com.infyniteloop.runningroom.dto.BuildingResponse;
import com.infyniteloop.runningroom.exception.BadRequestException;
import com.infyniteloop.runningroom.exception.DuplicateResourceException;
import com.infyniteloop.runningroom.exception.NotFoundException;
import com.infyniteloop.runningroom.model.Building;
import com.infyniteloop.runningroom.repository.BuildingRepository;
import com.infyniteloop.runningroom.repository.RunningRoomRepository;
import com.infyniteloop.runningroom.util.TenantContext;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BuildingServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(BuildingServiceImpl.class);
    private final BuildingRepository buildingRepository;
    private final RunningRoomRepository runningRoomRepository;
    private final EntityManager entityManager;

    public BuildingServiceImpl(BuildingRepository buildingRepository, RunningRoomRepository runningRoomRepository,
                               EntityManager entityManager) {
        this.buildingRepository = buildingRepository;
        this.runningRoomRepository = runningRoomRepository;
        this.entityManager = entityManager;
    }

    // Enable tenant filter for current session
    private void enableTenantFilter() {
        UUID tenantId = TenantContext.CURRENT_TENANT.get();
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
    }


    public List<BuildingResponse> getAllBuildings() {
        enableTenantFilter();
        return buildingRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BuildingResponse getBuildingById(UUID id) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Building not found"));
        return toResponse(building);
    }

    public BuildingResponse createBuilding(BuildingRequest request) {

        // Get tenantId from ThreadLocal
        UUID tenantId = TenantContext.getCurrentTenant();

        enableTenantFilter();
        runningRoomRepository.findById(tenantId)
                .orElseThrow(() -> new NotFoundException("RunningRoom not found"));

        //check before saving to provide a friendly error instead of waiting for the DB exception
        if (buildingRepository.existsByBuildingNameAndTenantId(request.buildingName(), tenantId)) {
            log.info("Building {} already exists for tenant {}", request.buildingName(), tenantId);
            throw new DuplicateResourceException("Building " + request.buildingName() + " already exists");
        }


        Building building = new Building();
        building.setBuildingName(request.buildingName());
        building.setAddress(request.address());
        building.setDescription(request.description());
        building.setTenantId(tenantId);

        return toResponse(buildingRepository.save(building));
    }

    public BuildingResponse updateBuilding(UUID id, BuildingRequest request) {

        enableTenantFilter();
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Building not found"));

        if (request.buildingName() != null) building.setBuildingName(request.buildingName());
        if (request.address() != null) building.setAddress(request.address());
        if (request.floors() != null) building.setFloors(request.floors());
        if (request.description() != null) building.setDescription(request.description());

        return toResponse(buildingRepository.save(building));
    }

    public void deleteBuilding(UUID id) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Building not found"));
        buildingRepository.delete(building);
    }

    private BuildingResponse toResponse(Building building) {
        return new BuildingResponse(
                building.getId(),
                building.getBuildingName(),
                building.getAddress(),
                building.getFloors(),
                building.getDescription(),
                building.getTenantId()
        );
    }
}
