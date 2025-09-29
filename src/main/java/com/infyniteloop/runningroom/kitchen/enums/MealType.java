package com.infyniteloop.runningroom.kitchen.enums;

import java.util.List;

public enum MealType {
    BREAKFAST,
    LUNCH,
    SNACKS,
    DINNER;

    public static List<MealType> orderedList() {
        return List.of(BREAKFAST, LUNCH, SNACKS, DINNER);
    }
}