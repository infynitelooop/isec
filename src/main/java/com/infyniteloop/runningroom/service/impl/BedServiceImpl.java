package com.infyniteloop.runningroom.service.impl;

import com.infyniteloop.runningroom.model.Bed;
import com.infyniteloop.runningroom.repository.BedRepository;
import com.infyniteloop.runningroom.service.BedService;
import com.infyniteloop.runningroom.util.TenantContext;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class BedServiceImpl implements BedService {
    private final BedRepository bedRepository;
    private final EntityManager entityManager;

    public BedServiceImpl(BedRepository bedRepository, EntityManager entityManager) {
        this.bedRepository = bedRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Bed create(Bed bed) {
        
        UUID tenantId = TenantContext.getCurrentTenant();

        bed.setTenantId(tenantId);
        return bedRepository.save(bed);
    }

    @Override
    public Bed update(UUID id, Bed updated) {
        UUID tenantId = TenantContext.getCurrentTenant();
        Bed existing = bedRepository.findById(id)
                .filter(b -> tenantId.equals(b.getTenantId()))
                .orElseThrow(() -> new IllegalArgumentException("Bed not found"));
        existing.setBedNumber(updated.getBedNumber());
        //existing.setBedStatus(updated.getBedStatus());
        //existing.setRoomId(updated.getRoomId());
        return bedRepository.save(existing);
    }

    @Override
    public List<Bed> listAll() {
        return bedRepository.findAll();
    }


    // other helpers (mark maintenance, etc.)
}