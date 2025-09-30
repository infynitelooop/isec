package com.infyniteloop.runningroom.service.impl;

import com.infyniteloop.runningroom.dto.RoomRequest;
import com.infyniteloop.runningroom.dto.RoomResponse;
import com.infyniteloop.runningroom.exception.DuplicateResourceException;
import com.infyniteloop.runningroom.exception.NotFoundException;
import com.infyniteloop.runningroom.model.Building;
import com.infyniteloop.runningroom.model.Room;
import com.infyniteloop.runningroom.model.mapper.RoomMapper;
import com.infyniteloop.runningroom.repository.BuildingRepository;
import com.infyniteloop.runningroom.repository.RoomRepository;
import com.infyniteloop.runningroom.service.RoomService;
import com.infyniteloop.runningroom.util.TenantContext;
import jakarta.persistence.EntityManager;
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
    private final BuildingRepository buildingRepository;
    private final RoomMapper roomMapper;
    private final EntityManager entityManager;

    public RoomServiceImpl(RoomRepository roomRepository, BuildingRepository buildingRepository, RoomMapper roomMapper, EntityManager entityManager) {
        this.entityManager = entityManager;
        this.roomRepository = roomRepository;
        this.buildingRepository = buildingRepository;
        this.roomMapper = roomMapper;
    }

    // TODO: try to move it to a common place

//    3. Enable Hibernate tenant filter separately
//
//    This belongs in a JPA config / listener, not the request filter.
//
//    For example, you can use a @Component that listens to transactions and applies the filter:
//
//            package com.infyniteloop.runningroom.tenant;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.hibernate.Session;
//import org.springframework.stereotype.Component;
//
//import jakarta.transaction.Transactional;
//
//    @Component
//    public class TenantFilterEnabler {
//
//        @PersistenceContext
//        private EntityManager entityManager;
//
//        @Transactional
//        public void enableTenantFilter() {
//            UUID tenantId = TenantContext.getCurrentTenant();
//            Session session = entityManager.unwrap(Session.class);
//            session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
//        }
//    }
//
//


    @Override
    public RoomResponse createRoom(RoomRequest roomRequest) {

        // Get tenantId from ThreadLocal
        UUID tenantId = TenantContext.getCurrentTenant();


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

        UUID tenantId = TenantContext.CURRENT_TENANT.get();
        if (tenantId == null) {
            throw new NotFoundException("TenantId not found in request context");
        }

        // Fetch existing room
        Room existingRoom = roomRepository.findById(roomRequest.id())
                .orElseThrow(() -> new NotFoundException(ROOM_NOT_FOUND));

        // Fetch the Building details
        Building building = buildingRepository.findById(UUID.fromString(roomRequest.buildingId()))
                .orElseThrow(() -> new NotFoundException("Building not found"));

        existingRoom.setBuilding(building);
        existingRoom.setTenantId(tenantId); // ensure tenantId is set

        // Map fields from DTO to entity
        RoomMapper.updateRoomFromDto(roomRequest, existingRoom);

        Room saved = roomRepository.save(existingRoom);
        return roomMapper.toResponse(saved);
    }

    @Override
    public RoomResponse deleteRoom(String roomId) {
        Room room = roomRepository.findById(UUID.fromString(roomId))
                .orElseThrow(() -> new NotFoundException(ROOM_NOT_FOUND));
        roomRepository.delete(room);
        return roomMapper.toResponse(room);
    }

    @Override
    public RoomResponse getRoom(String roomId) {
        Room room = roomRepository.findById(UUID.fromString(roomId))
                .orElseThrow(() -> new NotFoundException(ROOM_NOT_FOUND));
        return roomMapper.toResponse(room);
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        List<Room> roomList = roomRepository.findAllWithBuilding();
        return roomList.stream()
                .map(roomMapper::toResponse)
                .toList();
    }
}
