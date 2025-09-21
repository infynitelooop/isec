package com.infyniteloop.runningroom.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "building")
public class Building extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private String buildingName;
    private String address;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "running_room_id", nullable = false)
    private RunningRoom runningRoom;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;

}