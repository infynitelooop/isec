package com.infyniteloop.runningroom.room.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.infyniteloop.runningroom.model.BaseEntity;
import com.infyniteloop.runningroom.bed.entity.Bed;
import com.infyniteloop.runningroom.building.entity.Building;
import com.infyniteloop.runningroom.model.enums.AttachmentType;
import com.infyniteloop.runningroom.model.enums.CrewType;
import com.infyniteloop.runningroom.model.enums.RoomCategory;
import com.infyniteloop.runningroom.model.enums.RoomStatus;
import com.infyniteloop.runningroom.model.enums.RoomType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import java.util.List;
import java.util.UUID;


/**
 * * Entity representing a Room in the system.
 */

@Entity
@Table(name = "rooms", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"roomNumber", "tenantId"})
})
@Getter
@Setter
@NoArgsConstructor
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId") // Apply the filter condition
public class Room extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @NonNull
    @Column(nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType type = RoomType.SINGLE;

    private boolean ac = true; // AC or Non-AC

    private int capacity = 1;

    private int floor = 0;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Bed> beds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Enumerated(EnumType.STRING)
    private RoomStatus status = RoomStatus.AVAILABLE;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private CrewType crewType = CrewType.LOCO_PILOT; // e.g., LOCO PILOT, GUARD, ETC

    @Enumerated(EnumType.STRING)
    private RoomCategory roomCategory = RoomCategory.MALE; // MALE/FEMALE

    @Enumerated(EnumType.STRING)
    private AttachmentType attachment = AttachmentType.I; // Toilet type INDIAN/ WESTERN/ COMMON
}
