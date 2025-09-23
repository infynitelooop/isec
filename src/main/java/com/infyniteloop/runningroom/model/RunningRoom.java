package com.infyniteloop.runningroom.model;

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
    private String name;
    private Boolean subsidisedMeal;
    private String description;
    private String division;
    private String zone;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Building> buildings;
}