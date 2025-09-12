package com.infyniteloop.runningroom.dto;

import com.infyniteloop.runningroom.model.types.RoomStatus;
import com.infyniteloop.runningroom.model.types.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomRequest(
        @NotBlank(message = "roomNumber cannot be blank")
        String roomNumber,

        @NotNull(message = "roomType cannot be null")
        RoomType roomType,

        @Min(value = 1, message = "capacity must be at least 1")
        int capacity,

        @NotNull(message = "status cannot be null")
        RoomStatus status
) {}