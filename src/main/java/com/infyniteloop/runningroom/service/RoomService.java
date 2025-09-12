package com.infyniteloop.runningroom.service;

import com.infyniteloop.runningroom.model.Room;
import com.infyniteloop.runningroom.dto.RoomRequest;
import com.infyniteloop.runningroom.dto.RoomResponse;

import java.util.UUID;

public interface RoomService {

    void allocateRoom(String userId, String roomNumber);
    void deallocateRoom(String userId);
    RoomResponse createRoom(RoomRequest roomRequest, UUID tenantId);
    RoomResponse updateRoom(RoomRequest roomRequest);
    Room deleteRoom(String roomId);
    RoomResponse getRoomByNumber(String roomNumber);
}
