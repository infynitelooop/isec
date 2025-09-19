package com.infyniteloop.runningroom.dto;

import com.infyniteloop.runningroom.model.types.AttachmentType;
import com.infyniteloop.runningroom.model.types.CrewType;
import com.infyniteloop.runningroom.model.types.RoomCategory;
import com.infyniteloop.runningroom.model.types.RoomStatus;
import com.infyniteloop.runningroom.model.types.RoomType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RoomRequest(

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

        @Min(value = 0, message = "Beds cannot be negative")
        int beds,

        @NotBlank(message = "Building name is required")
        String buildingName,

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