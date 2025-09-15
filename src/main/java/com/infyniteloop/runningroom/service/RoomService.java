package com.infyniteloop.runningroom.service;

import com.infyniteloop.runningroom.dto.RoomRequest;
import com.infyniteloop.runningroom.dto.RoomResponse;
import com.infyniteloop.runningroom.model.Room;

public interface RoomService {

    RoomResponse createRoom(RoomRequest roomRequest);
    RoomResponse updateRoom(RoomRequest roomRequest);
    RoomResponse deleteRoom(String roomId);
    java.util.List<RoomResponse> getAllRooms();
    RoomResponse getRoom(String roomId);
}
