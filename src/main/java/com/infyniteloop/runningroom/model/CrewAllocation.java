package com.infyniteloop.runningroom.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "crew_allocation")
public class CrewAllocation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String crewId; // identifier for crew member
    private Instant checkInAt;
    private Instant checkOutAt;
    private String allocatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_id", nullable = false)
    private Bed bed;

    public boolean isActive() {
        return checkOutAt == null;
    }

    // getters & setters
}