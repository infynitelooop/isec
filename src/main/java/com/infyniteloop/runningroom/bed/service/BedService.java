package com.infyniteloop.runningroom.bed.service;

import com.infyniteloop.runningroom.bed.entity.Bed;

import java.util.List;
import java.util.UUID;

public interface BedService {
    Bed create(Bed bed);

    Bed update(UUID id, Bed updated);

    List<Bed> listAll();
}
