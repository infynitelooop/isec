package com.infyniteloop.runningroom.bed.dto;

import com.infyniteloop.runningroom.room.entity.Room;
import com.infyniteloop.runningroom.model.enums.OccupancyStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


import java.util.UUID;

public record BedRequest(

        UUID id,

        @NotBlank(message = "bedNumber cannot be blank")
        @Min(value = 0, message = "Floor cannot be negative")
        @Max(value = 10, message = "Floor cannot be greater than 10")
        String bedNumber,

        @NotBlank(message = "roomId cannot be blank")
        UUID roomId,

        OccupancyStatus occupancyStatus,
        Room room
) {
}