package com.infyniteloop.runningroom.model.mapper;

import com.infyniteloop.runningroom.model.Room;
import com.infyniteloop.runningroom.dto.RoomRequest;
import com.infyniteloop.runningroom.dto.RoomResponse;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    // request -> entity
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "roomType", target = "type")
    Room toEntity(RoomRequest request, @Context UUID tenantId); // TODO: Do we really ned tenantid here?

    // entity -> response
    RoomResponse toResponse(Room room);
}