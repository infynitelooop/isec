package com.infyniteloop.runningroom.crew.entity;

import com.infyniteloop.runningroom.booking.entity.Booking;
import com.infyniteloop.runningroom.enums.enums.Gender;
import com.infyniteloop.runningroom.enums.enums.RoomType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    private String fathersName;

    @Column(length = 10)
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(length = 12)
    private String mobileNumber;

    @Column(length = 100)
    private String address;

    @Column(length = 100)
    private String permanentAddress;

    @Column(length = 10)
    private String maritalStatus;

    @Column(length = 3)
    private String bloodGroup;

    @Column(length = 12)
    private String emergencyContactNumber;

    @Column(length = 30)
    private String designation;

    @Column(length = 4)
    private String crewType;   // Loco, Guard, etc.

    @Column(length = 4)
    private String orgType;

    @Column(length = 4)
    private String hqCode;

    @Column(length = 1)
    private String cadre;

    // Bi-directional relation with bookings
    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
}
