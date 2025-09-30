package com.infyniteloop.booking.entity;

import com.infyniteloop.runningroom.crew.entity.Crew;
import com.infyniteloop.runningroom.kitchen.enums.MealCategory;
import com.infyniteloop.runningroom.kitchen.enums.MealType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    private String signOffStation;
    private LocalDateTime signOffDateTime;
    private Integer restHours;

    private String taSno;
    private Boolean subsidizedFood;
    private String attachmentPref;

    private UUID tenantId;
    private UUID buildingId;
    private String ccId;
    private String ccUserId;

    private LocalDateTime signOffApprovalTime;

    private String bedId;
    private MealType mealType;     // B/L/D
    private MealCategory vegNonVeg;    // V/N
    private LocalDateTime checkInTime;
    private String checkInUserId;
    private LocalDateTime checkOutTime;
    private String checkOutUserId;

    private String signOnNoV;

    private LocalDateTime transactionTime;
    private Integer noOfMeals;
    private LocalDateTime wakeUpTime;
}
