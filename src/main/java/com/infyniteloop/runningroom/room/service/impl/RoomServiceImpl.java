package com.infyniteloop.runningroom.room.service.impl;

import com.infyniteloop.runningroom.bed.entity.Bed;
import com.infyniteloop.runningroom.booking.entity.Booking;
import com.infyniteloop.runningroom.booking.repository.BookingRepository;
import com.infyniteloop.runningroom.enums.enums.OccupancyStatus;
import com.infyniteloop.runningroom.room.dto.RoomRequest;
import com.infyniteloop.runningroom.room.dto.RoomResponse;
import com.infyniteloop.runningroom.exception.DuplicateResourceException;
import com.infyniteloop.runningroom.exception.NotFoundException;
import com.infyniteloop.runningroom.building.entity.Building;
import com.infyniteloop.runningroom.room.entity.Room;
import com.infyniteloop.runningroom.bed.repository.BedRepository;
import com.infyniteloop.runningroom.room.mapper.RoomMapper;
import com.infyniteloop.runningroom.building.repository.BuildingRepository;
import com.infyniteloop.runningroom.room.repository.RoomRepository;
import com.infyniteloop.runningroom.room.service.RoomService;
import com.infyniteloop.runningroom.util.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger log = LoggerFactory.getLogger(RoomServiceImpl.class);

    public static final String ROOM_NOT_FOUND = "Room not found";
    private final RoomRepository roomRepository;
    private final BuildingRepository buildingRepository;
    private final BookingRepository bookingRepository;
    private final BedRepository bedRepository;
    private final RoomMapper roomMapper;

    public RoomServiceImpl(RoomRepository roomRepository, BuildingRepository buildingRepository, RoomMapper roomMapper,
                           BookingRepository bookingRepository, BedRepository bedRepository) {
        this.roomRepository = roomRepository;
        this.buildingRepository = buildingRepository;
        this.roomMapper = roomMapper;
        this.bookingRepository = bookingRepository;
        this.bedRepository = bedRepository;
    }


    @Override
    @Transactional
    public RoomResponse createRoom(RoomRequest roomRequest) {

        // Get tenantId from ThreadLocal
        UUID tenantId = TenantContext.getCurrentTenant();
        Room room = roomMapper.toEntity(roomRequest, tenantId);

        //check before saving to provide a friendly error instead of waiting for the DB exception
        if (roomRepository.existsByRoomNumberAndTenantId(roomRequest.roomNumber(), tenantId)) {
            log.info("Room number already exists for tenant {}", tenantId);
            throw new DuplicateResourceException("Room number already exists");
        }

        // create beds to match capacity
        int capacity = room.getCapacity();
        List<Bed> beds = new ArrayList<>(capacity);
        for (int i = 1; i <= capacity; i++) {
            Bed bed = new Bed();
            // choose a bed-numbering scheme: RoomNumber-B1, B2...
            bed.setBedNumber(i);
            bed.setRoom(room);
            bed.setTenantId(tenantId); // if Bed entity has tenantId field
            bed.setOccupancyStatus(OccupancyStatus.AVAILABLE);
            beds.add(bed);
        }
        room.setBeds(beds);


        room.setTenantId(tenantId); // automatically set tenant
        Room saved = roomRepository.saveAndFlush(room);

        // Ensure beds are persisted (some JPA providers may need explicit save in certain edge cases)
        if (saved.getBeds() != null && !saved.getBeds().isEmpty()) {
            List<Bed> persistedBeds = bedRepository.saveAll(saved.getBeds());
            // Log persisted bed ids for debugging
            persistedBeds.forEach(b -> log.debug("Persisted bed id={} number={} for room={}", b.getId(), b.getBedNumber(), saved.getId()));
        }

        // Create default bookings for each bed
        List<Booking> bookings = new ArrayList<>(capacity);
        for (Bed bed : saved.getBeds()) {
            Booking booking = new Booking();
            booking.setBed(bed);
            booking.setOccupancyStatus(OccupancyStatus.AVAILABLE);
            booking.setTenantId(tenantId);
            bookings.add(booking);
        }
        bookingRepository.saveAll(bookings);
        log.info("Created room {} with {} beds and {} bookings", saved.getId(), capacity, capacity);

        return roomMapper.toResponse(saved);

    }

    @Override
    @Transactional
    public RoomResponse updateRoom(RoomRequest roomRequest) {

        UUID tenantId = TenantContext.CURRENT_TENANT.get();
        if (tenantId == null) {
            throw new NotFoundException("TenantId not found in request context");
        }

        // Fetch existing room
        Room existingRoom = roomRepository.findById(roomRequest.id())
                .orElseThrow(() -> new NotFoundException(ROOM_NOT_FOUND));

        // Map fields from DTO to entity
        // update scalar fields (keeps tenantId unchanged)
        RoomMapper.updateRoomFromDto(roomRequest, existingRoom);

        // Fetch the Building details
        Building building = buildingRepository.findById(UUID.fromString(roomRequest.buildingId()))
                .orElseThrow(() -> new NotFoundException("Building not found"));

        existingRoom.setBuilding(building);


        int desiredCapacity =roomRequest.bedCount();
        List<Bed> currentBeds = existingRoom.getBeds() == null ? new ArrayList<>() : existingRoom.getBeds();
        int currentCount = currentBeds.size();

        if (desiredCapacity > currentCount) {
            // add new beds
            List<Bed> newBeds = new ArrayList<>();
            for (int i = currentCount + 1; i <= desiredCapacity; i++) {
                Bed bed = new Bed();
                bed.setBedNumber(i);
                bed.setRoom(existingRoom);
                bed.setTenantId(tenantId);
                bed.setOccupancyStatus(OccupancyStatus.AVAILABLE);
                currentBeds.add(bed);
                newBeds.add(bed);
            }
            existingRoom.setBeds(currentBeds);

            // Explicitly persist and refresh new beds
            bedRepository.saveAllAndFlush(newBeds);

            // Save and flush room first so newly added beds are inserted and receive IDs
            roomRepository.saveAndFlush(existingRoom);

            // Create bookings for newly added beds
            List<Booking> newBookings = new ArrayList<>();
            for (Bed bed : newBeds) {
                Booking booking = new Booking();
                booking.setBed(bed);
                booking.setOccupancyStatus(OccupancyStatus.AVAILABLE);
                booking.setTenantId(tenantId);
                newBookings.add(booking);
            }
            bookingRepository.saveAll(newBookings);
            log.info("Added {} new beds and {} new bookings to room", newBeds.size(), newBookings.size());

            Room savedRoom = roomRepository.saveAndFlush(existingRoom);
            return roomMapper.toResponse(savedRoom);

        } else if (desiredCapacity < currentCount) {
            // remove surplus beds safely
            int toRemove = currentCount - desiredCapacity;
            // Identify beds to remove (from the end)
            List<Bed> bedsToRemove = new ArrayList<>();
            for (int i = currentBeds.size() - 1; i >= 0 && bedsToRemove.size() < toRemove; i--) {
                Bed b = currentBeds.get(i);
                bedsToRemove.add(b);
            }

            for (Bed bed : bedsToRemove) {
                if (!bed.getOccupancyStatus().equals(OccupancyStatus.AVAILABLE)) {
                    throw new IllegalStateException(
                            "Cannot reduce capacity: bed " + (bed != null ? bed.getBedNumber() : "unknown") +
                                    " has active bookings. Complete or cancel all bookings before reducing capacity."
                    );
                }
            }

            // Remove beds from the room
            for (Bed bed : bedsToRemove) {
                bedRepository.deleteById(bed.getId());
                currentBeds.remove(bed);
            }

            existingRoom.setBeds(currentBeds);

            // Delete bookings for beds being removed
            for (Bed bed : bedsToRemove) {
                bookingRepository.deleteByBedId(bed.getId());
                log.info("Deleted all bookings for bed {} during room update", bed.getId());
            }

            // With orphanRemoval = true, removed Bed entities will be deleted on save/flush
        }

        Room saved = roomRepository.saveAndFlush(existingRoom);
        return roomMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public RoomResponse deleteRoom(String roomId) {
        Room room = roomRepository.findById(UUID.fromString(roomId))
                .orElseThrow(() -> new NotFoundException(ROOM_NOT_FOUND));

        // Safety check: ensure no beds have active bookings
        List<Bed> beds = room.getBeds();

        for (Bed bed : beds) {
            if (!bed.getOccupancyStatus().equals(OccupancyStatus.AVAILABLE)) {
                throw new IllegalStateException(
                        "Cannot reduce capacity: bed " + (bed != null ? bed.getBedNumber() : "unknown") +
                                " has active bookings. Complete or cancel all bookings before reducing capacity."
                );
            }
        }

        // Delete all non-active bookings for beds in this room (cleanup)
        if (beds != null && !beds.isEmpty()) {
            for (Bed bed : beds) {
                bookingRepository.deleteByBedId(bed.getId());
                log.debug("Deleted all bookings for bed {} during room deletion", bed.getBedNumber());
            }
        }

        roomRepository.delete(room);
        log.info("Room {} deleted successfully with all associated beds and bookings", roomId);
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
