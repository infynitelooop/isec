package com.infyniteloop.runningroom.dto;

import com.infyniteloop.runningroom.model.CrewAllocation;
import com.infyniteloop.runningroom.model.Room;
import com.infyniteloop.runningroom.model.enums.OccupancyStatus;

import java.util.List;
import java.util.UUID;

public record BedResponse(
        UUID id,
        String bedNumber,
        UUID roomId,
        OccupancyStatus occupancyStatus,
        Room room,
        List<CrewAllocation> allocations) {
}
