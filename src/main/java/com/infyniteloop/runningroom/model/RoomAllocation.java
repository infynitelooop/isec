package com.infyniteloop.runningroom.model;

import com.infyniteloop.isec.security.models.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

//TODO: Need to remove it
@Entity
@Table(name = "room_allocations")
@Getter
@Setter
@NoArgsConstructor
public class RoomAllocation {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    @Column(nullable = false)
    private UUID tenantId;

    // getters and setters
}