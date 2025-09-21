package com.infyniteloop.runningroom.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "tenant_id", nullable = false, updatable = false)
    protected UUID tenantId;

    @Column(name = "created_at", nullable = false, updatable = false)
    protected Instant createdAt = Instant.now();

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}