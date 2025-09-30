package com.infyniteloop.booking.service;


import com.infyniteloop.booking.entity.Booking;
import com.infyniteloop.runningroom.crew.entity.Crew;
import com.infyniteloop.runningroom.crew.repository.BookingRepository;
import com.infyniteloop.runningroom.crew.repository.CrewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookingService {

    private final CrewRepository crewRepo;
    private final BookingRepository bookingRepo;

    public BookingService(CrewRepository crewRepo, BookingRepository bookingRepo) {
        this.crewRepo = crewRepo;
        this.bookingRepo = bookingRepo;
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

//    // Find active bookings for a building
//    public List<Booking> findActiveBookings(String buildingName) {
//        return bookingRepo.findByBuildingNameAndCheckOutTimeIsNull(buildingName);
//    }
}
