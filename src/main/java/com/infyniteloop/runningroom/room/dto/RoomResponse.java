package com.infyniteloop.runningroom.room.dto;

import com.infyniteloop.runningroom.enums.enums.AttachmentType;
import com.infyniteloop.runningroom.enums.enums.CrewType;
import com.infyniteloop.runningroom.enums.enums.RoomCategory;
import com.infyniteloop.runningroom.enums.enums.RoomStatus;
import com.infyniteloop.runningroom.enums.enums.RoomType;
import java.util.UUID;

public record RoomResponse(
        UUID id,
        String roomNumber,
        RoomType roomType,
        boolean ac,
        int capacity,
        int floor,
        int bedCount,
        String buildingId,
        String description,
        CrewType crewType,
        RoomCategory roomCategory,
        AttachmentType attachment,
        RoomStatus status,
        String tenantId   // optional: include only if admins need it
) {}