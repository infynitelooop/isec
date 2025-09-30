package com.infyniteloop.booking.controller;

import com.infyniteloop.booking.entity.Booking;
import com.infyniteloop.booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/{crewId}")
    public ResponseEntity<Booking> createBooking(
            @PathVariable String crewId,
            @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.createBooking(crewId, booking));
    }

    @PostMapping("/{bookingId}/check-in")
    public ResponseEntity<Booking> checkIn(
            @PathVariable Long bookingId,
            @RequestParam String userId) {
        return ResponseEntity.ok(bookingService.checkIn(bookingId, userId));
    }

    @PostMapping("/{bookingId}/check-out")
    public ResponseEntity<Booking> checkOut(
            @PathVariable Long bookingId,
            @RequestParam String userId) {
        return ResponseEntity.ok(bookingService.checkOut(bookingId, userId));
    }

//    @GetMapping("/active/{buildingName}")
//    public ResponseEntity<List<Booking>> getActiveBookings(
//            @PathVariable String buildingName) {
//        return ResponseEntity.ok(bookingService.findActiveBookings(buildingName));
//    }
}
