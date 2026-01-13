package com.infyniteloop.runningroom.booking.service;


import com.infyniteloop.runningroom.booking.dto.BookingResponse;
import com.infyniteloop.runningroom.booking.dto.BuildingOccupancyResponse;
import com.infyniteloop.runningroom.booking.dto.RoomOccupancyResponse;
import com.infyniteloop.runningroom.booking.entity.Booking;
import com.infyniteloop.runningroom.booking.mapper.BookingMapper;
import com.infyniteloop.runningroom.crew.entity.Crew;
import com.infyniteloop.runningroom.crew.repository.BookingRepository;
import com.infyniteloop.runningroom.crew.repository.CrewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class BookingService {

    private final CrewRepository crewRepo;
    private final BookingRepository bookingRepo;
    private final BookingMapper bookingMapper;

    public BookingService(CrewRepository crewRepo, BookingRepository bookingRepo, BookingMapper bookingMapper) {
        this.crewRepo = crewRepo;
        this.bookingRepo = bookingRepo;
        this.bookingMapper = bookingMapper;
    }

    // Get All Bookings
    public java.util.List<BookingResponse> getAllBookings() {
        // Find all records and convert to BookingResponse DTOs
        return bookingRepo.findAll().stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    // Get All Bookings
    public List<BuildingOccupancyResponse> getBookingsDashBoard() {
        // Find all records and convert to BookingResponse DTOs


        List<Booking> all = bookingRepo.findAll();
        List<BookingResponse> bookings = all.stream()
                .map(bookingMapper::toDto)
                .toList();

        // Group the bookings by building name then by room nameø
        return bookings.stream()
                .collect(Collectors.groupingBy(
                        BookingResponse::buildingName,
                        LinkedHashMap::new,
                        Collectors.groupingBy(
                                BookingResponse::roomNumber,
                                LinkedHashMap::new,
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .map(buildingEntry -> new BuildingOccupancyResponse(
                        buildingEntry.getKey(),
                        buildingEntry.getValue().entrySet().stream()
                                .map(roomEntry -> new RoomOccupancyResponse(
                                        roomEntry.getKey(),
                                        roomEntry.getValue()                     // List<BookingResponse>
                                ))
                                .toList()                                   // List<RoomOccupancyResponse>
                ))
                .toList();

    }


    // Create / allocate room
    public Booking createBooking(String crewId, Booking bookingRequest) {
        Crew crew = crewRepo.findById(crewId)
                .orElseThrow(() -> new IllegalArgumentException("Crew not found: " + crewId));
        bookingRequest.setCrew(crew);
        bookingRequest.setTransactionTime(LocalDateTime.now());
        return bookingRepo.save(bookingRequest);
    }

    // Check-in
    public Booking checkIn(Long bookingId, String userId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        booking.setCheckInTime(LocalDateTime.now());
        booking.setCheckInUserId(userId);
        return bookingRepo.save(booking);
    }

    // Check-out
    public Booking checkOut(Long bookingId, String userId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        booking.setCheckOutTime(LocalDateTime.now());
        booking.setCheckOutUserId(userId);
        return bookingRepo.save(booking);
    }


    public String generateBookingNumber(String crewId) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "BKG-" + date + "-" + crewId;
    }

//    // Find active bookings for a building
//    public List<Booking> findActiveBookings(String buildingName) {
//        return bookingRepo.findByBuildingNameAndCheckOutTimeIsNull(buildingName);
//    }
}
