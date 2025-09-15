package com.infyniteloop.runningroom.service.impl;

import com.infyniteloop.runningroom.dto.RoomRequest;
import com.infyniteloop.runningroom.dto.RoomResponse;
import com.infyniteloop.runningroom.model.Room;
import com.infyniteloop.runningroom.model.mapper.RoomMapper;
import com.infyniteloop.runningroom.repository.RoomRepository;
import com.infyniteloop.runningroom.security.TenantFilter;
import com.infyniteloop.runningroom.service.RoomService;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final EntityManager entityManager;

    public RoomServiceImpl(RoomRepository roomRepository, RoomMapper roomMapper, EntityManager entityManager) {
        this.entityManager = entityManager;
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    // Enable tenant filter for current session
    private void enableTenantFilter() {
        UUID tenantId = TenantFilter.CURRENT_TENANT.get();
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
    }

    @Override
    public RoomResponse createRoom(RoomRequest roomRequest) {

        // Get tenantId from ThreadLocal
        UUID tenantId = TenantFilter.CURRENT_TENANT.get();
        if (tenantId == null) {
            throw new IllegalStateException("No tenant context found");
        }

        //check before saving to provide a friendly error instead of waiting for the DB exception
        if (roomRepository.existsByRoomNumberAndTenantId(roomRequest.roomNumber(), tenantId)) {
            throw new IllegalArgumentException("Room number already exists");
        }

        Room room = roomMapper.toEntity(roomRequest, tenantId);
        room.setTenantId(tenantId); // automatically set tenant
        Room saved = roomRepository.save(room);
        return roomMapper.toResponse(saved);

    }

    @Override
    public RoomResponse updateRoom(RoomRequest roomRequest) {

        UUID tenantId = TenantFilter.CURRENT_TENANT.get();
        if (tenantId == null) {
            throw new IllegalStateException("TenantId not found in request context");
        }

        enableTenantFilter();
        // Fetch existing room
        Room existingRoom = roomRepository.findByRoomNumber(roomRequest.roomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Map fields from DTO to entity
        RoomMapper.updateRoomFromDto(roomRequest, existingRoom);

        Room saved = roomRepository.save(existingRoom);
        return roomMapper.toResponse(saved);
    }

    @Override
    public RoomResponse deleteRoom(String roomId) {
        enableTenantFilter();
        Room room = roomRepository.findById(UUID.fromString(roomId))
                .orElseThrow(() -> new RuntimeException("Room not found"));
        roomRepository.delete(room);
        return roomMapper.toResponse(room);
    }

    @Override
    public RoomResponse getRoom(String roomId) {
        enableTenantFilter();
        Room room = roomRepository.findById(UUID.fromString(roomId))
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return roomMapper.toResponse(room);
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        enableTenantFilter();
        return roomRepository.findAll().stream()
                .map(roomMapper::toResponse)
                .toList();
    }
}
