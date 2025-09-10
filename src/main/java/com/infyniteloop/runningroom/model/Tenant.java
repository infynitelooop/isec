package com.infyniteloop.runningroom.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
public class Tenant {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String tenantName;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Tenant(String tenantName) {
        this.tenantName = tenantName;
    }
}
