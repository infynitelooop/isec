package com.infyniteloop.runningroom.booking.dto;

import java.util.List;

public record RoomOccupancyResponse(
        String roomNumber,
        List<BookingResponse> beds
) {}