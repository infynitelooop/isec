package com.infyniteloop.runningroom.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "building", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"buildingName", "tenantId"})
})// Define the filter
@NoArgsConstructor
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId") // Apply the filter condition
public class Building extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private String buildingName;
    private String address;
    private Integer floors;
    private String description;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;

}