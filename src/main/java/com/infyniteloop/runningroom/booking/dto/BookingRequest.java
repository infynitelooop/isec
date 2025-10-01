package com.infyniteloop.runningroom.booking.dto;

import java.time.LocalDateTime;

public record BookingRequest(
        Long bookingId,
        String occupancyStatus,
        String bedId,      // references Bed
        String crewId,     // references Crew
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