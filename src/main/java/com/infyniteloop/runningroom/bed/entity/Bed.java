package com.infyniteloop.runningroom.bed.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.infyniteloop.runningroom.model.BaseEntity;
import com.infyniteloop.runningroom.model.enums.OccupancyStatus;
import com.infyniteloop.runningroom.room.entity.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "bed", uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "bed_number"}))
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId") // Apply the filter condition
public class Bed extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String bedNumber;
    private OccupancyStatus occupancyStatus; // AVAILABLE, OCCUPIED, MAINTENANCE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonBackReference
    private Room room;

}