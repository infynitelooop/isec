package com.infyniteloop.runningroom.booking.entity;

import com.infyniteloop.runningroom.crew.entity.Crew;
import com.infyniteloop.runningroom.kitchen.enums.MealCategory;
import com.infyniteloop.runningroom.kitchen.enums.MealType;
import com.infyniteloop.runningroom.model.BaseEntity;
import com.infyniteloop.runningroom.bed.entity.Bed;
import com.infyniteloop.runningroom.model.enums.OccupancyStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import java.time.LocalDateTime;


@Entity
@Table(name = "booking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId") // Apply the filter condition
public class Booking extends BaseEntity {

    @PrePersist
    public void prePersist() {
        if (occupancyStatus == null) {
            occupancyStatus = OccupancyStatus.AVAILABLE;
        }
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'AVAILABLE'")
    private OccupancyStatus occupancyStatus = OccupancyStatus.AVAILABLE; // BOOKED, OCCUPIED, VACANT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_id")
    private Bed bed;


    private MealType mealType;     // B/L/D
    private MealCategory vegNonVeg;    // V/N
    private LocalDateTime checkInTime;
    private String checkInUserId;
    private LocalDateTime checkOutTime;
    private String checkOutUserId;
    private Boolean subsidizedFood;
    private String attachmentPref;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;

    private String signOffStation;
    private LocalDateTime signOffDateTime;
    private Integer restHours;

    private String taSno;

    private String ccId;
    private String ccUserId;

    private LocalDateTime signOffApprovalTime;

    private String signOnNoV;
    private LocalDateTime transactionTime;
    private Integer noOfMeals;
    private LocalDateTime wakeUpTime;
}
