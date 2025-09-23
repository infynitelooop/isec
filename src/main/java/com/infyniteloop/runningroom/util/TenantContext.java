package com.infyniteloop.runningroom.util;

import com.infyniteloop.runningroom.exception.NotFoundException;

import java.util.Optional;
import java.util.UUID;

public class TenantContext {
    public static final ThreadLocal<UUID> CURRENT_TENANT = new ThreadLocal<>();

    public static void setCurrentTenant(UUID tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static UUID getCurrentTenant() {
        UUID tenantId = CURRENT_TENANT.get();
        if (tenantId == null) {
            throw new NotFoundException("TenantId not found in request context");
        }
        return Optional.ofNullable(tenantId).orElseThrow(() -> new NotFoundException("TenantId not found in request context"));
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}