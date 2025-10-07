package com.infyniteloop.runningroom.booking.mapper;


import com.infyniteloop.runningroom.booking.dto.BookingRequest;
import com.infyniteloop.runningroom.booking.dto.BookingResponse;
import com.infyniteloop.runningroom.booking.entity.Booking;
import com.infyniteloop.runningroom.bed.entity.Bed;
import com.infyniteloop.runningroom.crew.entity.Crew;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    // ---- ENTITY -> READ DTO ----
    @Mapping(target = "bedNumber", source = "bed.bedNumber")
    @Mapping(target = "roomNumber", source = "bed.room.roomNumber")
    @Mapping(target = "buildingName", source = "bed.room.building.buildingName")
    @Mapping(target = "crewId", source = "crew.crewId")
    @Mapping(target = "crewName", source = "crew.name")
    @Mapping(target = "crewDesignation", source = "crew.designation")
    @Mapping(target = "crewType", source = "crew.crewType")
    BookingResponse toDto(Booking booking);

    // ---- REQUEST DTO -> ENTITY ----
    @Mapping(target = "bed", expression = "java(toBed(dto.bedId()))")
    @Mapping(target = "crew", expression = "java(toCrew(dto.crewId()))")
    Booking toEntity(BookingRequest dto);

    // ---- Helpers for relationships ----
    default Bed toBed(String bedId) {
        if (bedId == null) return null;
        Bed bed = new Bed();
        bed.setId(java.util.UUID.fromString(bedId));
        return bed; // only id is set, service will hydrate
    }

    default Crew toCrew(String crewId) {
        if (crewId == null) return null;
        Crew crew = new Crew();
        crew.setCrewId(crewId);
        return crew; // only id is set
    }
}
