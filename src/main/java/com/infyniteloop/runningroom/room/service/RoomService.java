package com.infyniteloop.runningroom.room.service;

import com.infyniteloop.runningroom.room.dto.RoomRequest;
import com.infyniteloop.runningroom.room.dto.RoomResponse;

public interface RoomService {

    RoomResponse createRoom(RoomRequest roomRequest);
    RoomResponse updateRoom(RoomRequest roomRequest);
    RoomResponse deleteRoom(String roomId);
    java.util.List<RoomResponse> getAllRooms();
    RoomResponse getRoom(String roomId);
}
