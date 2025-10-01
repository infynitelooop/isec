package com.infyniteloop.runningroom.booking.dto;

import java.time.LocalDateTime;

public record BookingResponse(
        Long bookingId,
        String occupancyStatus,
        String bedNumber,
        String roomNumber,
        String buildingName,
        String crewId,
        String crewName,
        String crewDesignation,
        String crewType,
        String mealType,
        String vegNonVeg,
        LocalDateTime checkInTime,
        LocalDateTime checkOutTime,
        Boolean subsidizedFood,
        String attachmentPref,
        String signOffStation,
        LocalDateTime signOffDateTime,
        Integer restHours,
        String taSno,
        String ccId,
        String ccUserId,
        LocalDateTime signOffApprovalTime,
        String signOnNoV,
        LocalDateTime transactionTime,
        Integer noOfMeals,
        LocalDateTime wakeUpTime
) {}