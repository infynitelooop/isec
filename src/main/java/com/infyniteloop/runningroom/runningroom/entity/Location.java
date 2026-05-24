package com.infyniteloop.runningroom.runningroom.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue
    private UUID locationId;


    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id")
    private Division division;
}