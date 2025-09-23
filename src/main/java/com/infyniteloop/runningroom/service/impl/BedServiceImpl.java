package com.infyniteloop.runningroom.service.impl;

import com.infyniteloop.runningroom.exception.NotFoundException;
import com.infyniteloop.runningroom.model.Bed;
import com.infyniteloop.runningroom.repository.BedRepository;
import com.infyniteloop.runningroom.security.TenantFilter;
import com.infyniteloop.runningroom.util.TenantContext;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class BedServiceImpl {
    private final BedRepository bedRepository;
    private final EntityManager entityManager;

    public BedServiceImpl(BedRepository bedRepository, EntityManager entityManager) {
        this.bedRepository = bedRepository;
        this.entityManager = entityManager;
    }

    // Enable tenant filter for current session
    private void enableTenantFilter() {
        UUID tenantId = TenantContext.CURRENT_TENANT.get();
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
    }



    public Bed create(Bed bed) {
        
        UUID tenantId = TenantContext.getCurrentTenant();
        enableTenantFilter();

        bed.setTenantId(tenantId);
        return bedRepository.save(bed);
    }

    public Bed update(UUID id, Bed updated) {
        UUID tenantId = TenantContext.getCurrentTenant();
        enableTenantFilter();
        Bed existing = bedRepository.findById(id)
                .filter(b -> tenantId.equals(b.getTenantId()))
                .orElseThrow(() -> new IllegalArgumentException("Bed not found"));
        existing.setBedNumber(updated.getBedNumber());
        //existing.setBedStatus(updated.getBedStatus());
        //existing.setRoomId(updated.getRoomId());
        return bedRepository.save(existing);
    }

    public List<Bed> listAll() {
        enableTenantFilter();
        return bedRepository.findAll();
    }


    // other helpers (mark maintenance, etc.)
}