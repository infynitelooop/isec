package com.infyniteloop.runningroom.building.repository;

import com.infyniteloop.runningroom.building.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BuildingRepository extends JpaRepository<Building, UUID> {
    Optional<Building> findByBuildingName(String name);
    boolean existsByBuildingNameAndTenantId(String buildingName, UUID tenantId);
}