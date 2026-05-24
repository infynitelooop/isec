package com.infyniteloop.runningroom.aop;

import com.infyniteloop.runningroom.util.TenantContext;
import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class TenantFilterAspect {

    private final EntityManager entityManager;

    public TenantFilterAspect(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before("execution(* com.infyniteloop.runningroom.service..*(..)) || " +
            "execution(* com.infyniteloop.runningroom.kitchen.service..*(..)) || " +
            "execution(* com.infyniteloop.runningroom.room.service..*(..)) || " +
            "execution(* com.infyniteloop.runningroom.booking.service..*(..)) || " +
            "execution(* com.infyniteloop.runningroom.building.service..*(..)) || " +
            "execution(* com.infyniteloop.isec.security.services..*(..))")
    public void enableTenantFilter() {
        UUID tenantId = null;
        try {
            tenantId = TenantContext.getCurrentTenant();
        } catch (RuntimeException e) {
            // Tenant not present in context (e.g., public endpoints like signin).
            // Skip enabling the filter in this case.
            return;
        }

        if (tenantId != null) {
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
        }

    }
}