package com.infyniteloop.runningroom.runningroom.entity;

import com.infyniteloop.runningroom.building.entity.Building;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "running_room")
public class RunningRoom {
    @Id @GeneratedValue
    private UUID id;
    //location name
    private String name;
    private Boolean subsidisedMeal;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id")
    private Division division;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Building> buildings;
}