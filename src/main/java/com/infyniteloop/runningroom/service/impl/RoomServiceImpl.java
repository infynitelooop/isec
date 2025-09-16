package com.infyniteloop.runningroom.service.impl;

import com.infyniteloop.runningroom.dto.RoomRequest;
import com.infyniteloop.runningroom.dto.RoomResponse;
import com.infyniteloop.runningroom.exception.DuplicateResourceException;
import com.infyniteloop.runningroom.exception.NotFoundException;
import com.infyniteloop.runningroom.model.Room;
import com.infyniteloop.runningroom.model.mapper.RoomMapper;
import com.infyniteloop.runningroom.repository.RoomRepository;
import com.infyniteloop.runningroom.security.TenantFilter;
import com.infyniteloop.runningroom.service.RoomService;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger log = LoggerFactory.getLogger(RoomServiceImpl.class);

    public static final String ROOM_NOT_FOUND = "Room not found";
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
            throw new NotFoundException("No tenant context found");
        }

        //check before saving to provide a friendly error instead of waiting for the DB exception
        if (roomRepository.existsByRoomNumberAndTenantId(roomRequest.roomNumber(), tenantId)) {
            log.info("Room number already exists for tenant {}", tenantId);
            throw new DuplicateResourceException("Room number already exists");
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
            throw new NotFoundException("TenantId not found in request context");
        }

        enableTenantFilter();
        // Fetch existing room
        Room existingRoom = roomRepository.findByRoomNumber(roomRequest.roomNumber())
                .orElseThrow(() -> new NotFoundException(ROOM_NOT_FOUND));

        // Map fields from DTO to entity
        RoomMapper.updateRoomFromDto(roomRequest, existingRoom);

        Room saved = roomRepository.save(existingRoom);
        return roomMapper.toResponse(saved);
    }

    @Override
    public RoomResponse deleteRoom(String roomId) {
        enableTenantFilter();
        Room room = roomRepository.findById(UUID.fromString(roomId))
                .orElseThrow(() -> new NotFoundException(ROOM_NOT_FOUND));
        roomRepository.delete(room);
        return roomMapper.toResponse(room);
    }

    @Override
    public RoomResponse getRoom(String roomId) {
        enableTenantFilter();
        Room room = roomRepository.findById(UUID.fromString(roomId))
                .orElseThrow(() -> new NotFoundException(ROOM_NOT_FOUND));
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
