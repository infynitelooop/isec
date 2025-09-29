package com.infyniteloop.runningroom.kitchen.entity;

import com.infyniteloop.runningroom.kitchen.enums.MealType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WeeklyMenuItem {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;   // MONDAY, TUESDAY, etc

    @Enumerated(EnumType.STRING)
    private MealType mealType;     // BREAKFAST, LUNCH, etc

    private String name;
    private String description;
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private WeeklyMenuTemplate template;
}
