package com.infyniteloop.runningroom.building.dto;

import java.util.UUID;

public record BuildingResponse(
        UUID id,
        String buildingName,
        String address,
        Integer floors,
        String description,
        UUID tenantId
) {}
