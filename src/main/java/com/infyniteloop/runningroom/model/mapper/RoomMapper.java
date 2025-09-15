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

    static void updateRoomFromDto(RoomRequest roomRequest, Room existingRoom) {
        existingRoom.setCapacity(roomRequest.capacity());
        existingRoom.setStatus(roomRequest.status());
        existingRoom.setType(roomRequest.roomType());
    }

    // request -> entity
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "roomType", target = "type")
    // TODO: Do we really ned tenantid here?
    Room toEntity(RoomRequest request, @Context UUID tenantId);

    // entity -> response

    @Mapping(source = "type", target = "roomType")
    RoomResponse toResponse(Room room);
}