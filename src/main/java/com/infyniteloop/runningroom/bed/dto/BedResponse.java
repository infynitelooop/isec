package com.infyniteloop.runningroom.bed.dto;

import com.infyniteloop.runningroom.room.entity.Room;
import com.infyniteloop.runningroom.model.enums.OccupancyStatus;

import java.util.UUID;

public record BedResponse(
        UUID id,
        String bedNumber,
        UUID roomId,
        OccupancyStatus occupancyStatus,
        Room room) {
}
