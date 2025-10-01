package com.infyniteloop.runningroom.room.dto;

import com.infyniteloop.runningroom.model.enums.AttachmentType;
import com.infyniteloop.runningroom.model.enums.CrewType;
import com.infyniteloop.runningroom.model.enums.RoomCategory;
import com.infyniteloop.runningroom.model.enums.RoomStatus;
import com.infyniteloop.runningroom.model.enums.RoomType;
import java.util.UUID;

public record RoomResponse(
        UUID id,
        String roomNumber,
        RoomType roomType,
        boolean ac,
        int capacity,
        int floor,
        int beds,
        String buildingId,
        String description,
        CrewType crewType,
        RoomCategory roomCategory,
        AttachmentType attachment,
        RoomStatus status,
        String tenantId   // optional: include only if admins need it
) {}