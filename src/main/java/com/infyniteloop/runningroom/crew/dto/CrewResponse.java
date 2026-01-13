package com.infyniteloop.runningroom.crew.dto;

import java.time.LocalDate;

public record CrewResponse(
        String crewId,
        String name,
        String fathersName,
        String gender,
        LocalDate dateOfBirth,
        String mobileNumber,
        String address,
        String permanentAddress,
        String maritalStatus,
        String bloodGroup,
        String emergencyContactNumber,
        String designation,
        String crewType,
        String orgType,
        String hqCode,
        String cadre
) {}