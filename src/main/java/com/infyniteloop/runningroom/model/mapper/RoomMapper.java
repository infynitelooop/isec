package com.infyniteloop.runningroom.model.mapper;

import com.infyniteloop.runningroom.dto.RoomRequest;
import com.infyniteloop.runningroom.dto.RoomResponse;
import com.infyniteloop.runningroom.model.Bed;
import com.infyniteloop.runningroom.model.Room;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    static void updateRoomFromDto(RoomRequest roomRequest, Room existingRoom) {
        existingRoom.setCapacity(roomRequest.capacity());
        existingRoom.setStatus(roomRequest.status());
        existingRoom.setType(roomRequest.roomType());
        existingRoom.setAc(roomRequest.ac());
        existingRoom.setCrewType(roomRequest.crewType());
        existingRoom.setDescription(roomRequest.description());
        existingRoom.setFloor(roomRequest.floor());
        existingRoom.setRoomCategory(roomRequest.roomCategory());
        existingRoom.setRoomNumber(roomRequest.roomNumber());
        existingRoom.setAttachment(roomRequest.attachment());

        // --- Bed count logic ---
        int requestedBedCount = roomRequest.bedCount();
        List<Bed> beds = existingRoom.getBeds();
        int currentBedCount = beds.size();

        if (requestedBedCount > currentBedCount) {
            // Add new beds
            for (int i = currentBedCount + 1; i <= requestedBedCount; i++) {
                Bed bed = new Bed();
                bed.setBedNumber(i);
                bed.setRoom(existingRoom);
                bed.setTenantId(existingRoom.getTenantId());
                beds.add(bed);
            }
        } else if (requestedBedCount < currentBedCount) {
            // Remove extra beds
            beds.subList(requestedBedCount, currentBedCount).clear();
        }
        // --- End bed count logic ---

    }

    // request -> entity
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "roomType", target = "type")
    @Mapping(source = "buildingId", target = "building.id")
    // TODO: Do we really ned tenantid here?
    Room toEntity(RoomRequest request, @Context UUID tenantId);

    // entity -> response

    @Mapping(target = "beds", expression = "java(room.getBeds() != null ? room.getBeds().size() : 0)")
    @Mapping(target = "buildingId", source = "building.id")
    @Mapping(source = "type", target = "roomType")
    RoomResponse toResponse(Room room);
}