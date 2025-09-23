package com.infyniteloop.runningroom.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record BuildingRequest(

        UUID id,
        @NotBlank(message = "Building name is required")
        String buildingName,

        String address,
        Integer floors,
        String description,
        UUID tenantId
) {}
