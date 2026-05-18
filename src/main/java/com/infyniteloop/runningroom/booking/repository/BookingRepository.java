package com.infyniteloop.runningroom.booking.repository;


import com.infyniteloop.runningroom.bed.entity.Bed;
import com.infyniteloop.runningroom.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Find the latest booking (by bookingId) for the given bed id.
     * Use this to infer the current occupancy status for a bed.
     */
    Optional<Booking> findFirstByBed_IdOrderByBookingIdDesc(UUID bedId);


    Optional<Booking> findByBed(Bed bed);

//    /**
//     * Returns true if there exists an active booking (no checkout time) for the bed.
//     */
//    boolean existsByBed_IdAndCheckOutTimeIsNull(UUID bedId);

    /**
     * Find all bookings for a specific bed.
     */
    List<Booking> findAllByBed_Id(UUID bedId);

    /**
     * Delete all bookings for a specific bed (used when reducing room capacity).
     */
    @Modifying
    @Query("DELETE FROM Booking b WHERE b.bed.id = :bedId")
    void deleteByBedId(@Param("bedId") UUID bedId);

    /**
     * Find all bookings for beds in a given list of bed IDs.
     */
    List<Booking> findAllByBed_IdIn(List<UUID> bedIds);
}