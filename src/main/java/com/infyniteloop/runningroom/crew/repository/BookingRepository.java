package com.infyniteloop.runningroom.crew.repository;


import com.infyniteloop.runningroom.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}