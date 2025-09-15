package com.infyniteloop.runningroom.dto;

import com.infyniteloop.runningroom.model.types.RoomStatus;
import com.infyniteloop.runningroom.model.types.RoomType;

import java.util.UUID;

public record RoomResponse(
        UUID id,
        String roomNumber,
        RoomType roomType,
        int capacity,
        RoomStatus status,
        String tenantId   // optional: include only if admins need it
) {}