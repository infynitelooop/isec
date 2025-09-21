package com.infyniteloop.runningroom.dto;

import com.infyniteloop.runningroom.model.CrewAllocation;
import com.infyniteloop.runningroom.model.Room;
import com.infyniteloop.runningroom.model.enums.OccupancyStatus;
import jakarta.validation.constraints.NotBlank;


import java.util.List;
import java.util.UUID;

public record BedRequest(

        UUID id,

        @NotBlank(message = "bedNumber cannot be blank")
        String bedNumber,

        @NotBlank(message = "roomId cannot be blank")
        UUID roomId,

        OccupancyStatus occupancyStatus,
        Room room,

        List<CrewAllocation> allocations
) {
}