package com.infyniteloop.runningroom.crew.entity;

import com.infyniteloop.runningroom.booking.entity.Booking;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "crew")
@Getter
@Setter
public class Crew {
    @Id
    @Column(length = 10)
    private String crewId;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 30)
    private String designation;

    @Column(length = 4)
    private String type;   // Loco, Guard, etc.

    @Column(length = 4)
    private String orgType;

    @Column(length = 1)
    private String gender;

    @Column(length = 12)
    private String mobileNumber;

    @Column(length = 4)
    private String hqCode;

    @Column(length = 1)
    private String cadre;

    // Bi-directional relation with bookings
    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
}
