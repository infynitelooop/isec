package com.infyniteloop.runningroom.service.impl;

import com.infyniteloop.runningroom.model.Room;
import com.infyniteloop.runningroom.model.RoomAllocation;
import com.infyniteloop.runningroom.dto.RoomRequest;
import com.infyniteloop.runningroom.dto.RoomResponse;
import com.infyniteloop.runningroom.model.mapper.RoomMapper;
import com.infyniteloop.runningroom.repository.RoomRepository;
import com.infyniteloop.runningroom.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomServiceImpl(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }
    @Override
    public void allocateRoom(String userId, String roomNumber) {
        // Implementation for allocating a room to a user
        RoomAllocation roomAllocation = new RoomAllocation();



        // Set properties of roomAllocation as needed

        // Save roomAllocation to the database

    }

    @Override
    public void deallocateRoom(String userId) {

    }

    @Override
    public RoomResponse createRoom(RoomRequest roomRequest, UUID tenantId) {

        Room room = roomMapper.toEntity(roomRequest, tenantId);
        Room saved = roomRepository.save(room);
        return roomMapper.toResponse(saved);

    }

    @Override
    public RoomResponse updateRoom(RoomRequest roomRequest) {
        return null;
    }

    @Override
    public Room deleteRoom(String roomId) {
        return null;
    }

    @Override
    public RoomResponse getRoomByNumber(String roomNumber) {
        return null;
    }
}
