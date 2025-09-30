package com.infyniteloop.runningroom.crew.repository;


import com.infyniteloop.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}