package com.infyniteloop.runningroom.model;

import com.infyniteloop.runningroom.model.types.RoomType;
import com.infyniteloop.runningroom.model.types.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType type = RoomType.SINGLE;

    private int capacity = 2;

    @Enumerated(EnumType.STRING)
    private Status status = Status.VACANT;

    @Column(nullable = false)
    private UUID tenantId;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Room(String roomNumber, UUID tenantId) {
        this.roomNumber = roomNumber;
        this.tenantId = tenantId;
    }
}
