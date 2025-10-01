package com.infyniteloop.runningroom.room.dto;

import com.infyniteloop.runningroom.model.enums.AttachmentType;
import com.infyniteloop.runningroom.model.enums.CrewType;
import com.infyniteloop.runningroom.model.enums.RoomCategory;
import com.infyniteloop.runningroom.model.enums.RoomStatus;
import com.infyniteloop.runningroom.model.enums.RoomType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RoomRequest(

        UUID id,

        @NotBlank(message = "roomNumber cannot be blank")
        String roomNumber,

        @NotNull(message = "roomType cannot be null")
        RoomType roomType,

        boolean ac,

        @Min(value = 0, message = "capacity cannot be negative")
        @Max(value = 10, message = "capacity cannot be greater than 10")
        int capacity,

        @Min(value = 0, message = "Floor cannot be negative")
        @Max(value = 10, message = "Floor cannot be greater than 10")
        int floor,

        @NotBlank(message = "Building Id is required")
        String buildingId,

        @NotNull(message = "status cannot be null")
        RoomStatus status,

        @Size(max = 500, message = "Description must be at most 500 characters")
        String description,

        @NotNull(message = "Crew type is required")
        CrewType crewType,

        @NotNull(message = "Room category is required")
        RoomCategory roomCategory,

        @NotNull(message = "Attachment type is required")
        AttachmentType attachment

) {
}