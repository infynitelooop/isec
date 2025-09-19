package com.infyniteloop.runningroom.dto;

import com.infyniteloop.runningroom.model.types.AttachmentType;
import com.infyniteloop.runningroom.model.types.CrewType;
import com.infyniteloop.runningroom.model.types.RoomCategory;
import com.infyniteloop.runningroom.model.types.RoomStatus;
import com.infyniteloop.runningroom.model.types.RoomType;

import java.util.UUID;

public record RoomResponse(
        UUID id,
        String roomNumber,
        RoomType roomType,
        boolean ac,
        int capacity,
        int beds,
        int floor,
        String buildingName,
        String description,
        CrewType crewType,
        RoomCategory roomCategory,
        AttachmentType attachment,
        RoomStatus status,
        String tenantId   // optional: include only if admins need it
) {}