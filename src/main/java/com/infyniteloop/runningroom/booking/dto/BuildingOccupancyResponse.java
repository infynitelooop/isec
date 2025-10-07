package com.infyniteloop.runningroom.booking.dto;

import java.util.List;

public record BuildingOccupancyResponse(
        String buildingName,
        List<RoomOccupancyResponse> rooms
) {}