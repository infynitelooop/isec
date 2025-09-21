package com.infyniteloop.runningroom.model;

import com.infyniteloop.runningroom.model.enums.OccupancyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "bed")
public class Bed extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String bedNumber;
    private OccupancyStatus occupancyStatus; // AVAILABLE, OCCUPIED, MAINTENANCE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "bed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CrewAllocation> allocations;

}