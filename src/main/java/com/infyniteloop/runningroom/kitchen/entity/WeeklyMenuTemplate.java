package com.infyniteloop.runningroom.kitchen.entity;

import com.infyniteloop.runningroom.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "weekly_menu_templates")
public class WeeklyMenuTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name; // e.g. "Default Veg Week", "Festival Special"

    private boolean active = true; // Only one active template at a time

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeeklyMenuItem> items = new ArrayList<>();
}
