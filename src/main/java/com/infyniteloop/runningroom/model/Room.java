package com.infyniteloop.runningroom.model;

import com.infyniteloop.runningroom.model.types.RoomType;
import com.infyniteloop.runningroom.model.types.RoomStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 ** Entity representing a Room in the system.
 */

@Entity
@Table(name = "rooms", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"roomNumber", "tenantId"})
})
@Getter
@Setter
@NoArgsConstructor
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = UUID.class)) // Define the filter
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId") // Apply the filter condition
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
    private RoomStatus status = RoomStatus.AVAILABLE;

    @Column(nullable = false)
    private UUID tenantId;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Room(String roomNumber, UUID tenantId) {
        this.roomNumber = roomNumber;
        this.tenantId = tenantId;
    }
}
