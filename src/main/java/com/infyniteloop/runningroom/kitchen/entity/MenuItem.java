package com.infyniteloop.runningroom.kitchen.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.infyniteloop.runningroom.kitchen.enums.MealCategory;
import com.infyniteloop.runningroom.kitchen.enums.MealType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String description;
    private Double price;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @Enumerated(EnumType.STRING)
    private MealCategory mealCategory;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    @JsonBackReference
    private Menu menu;
}
