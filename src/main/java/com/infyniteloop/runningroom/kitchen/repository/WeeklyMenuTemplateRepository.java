package com.infyniteloop.runningroom.kitchen.repository;

import com.infyniteloop.runningroom.kitchen.entity.WeeklyMenuTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WeeklyMenuTemplateRepository extends JpaRepository<WeeklyMenuTemplate, UUID> {
    Optional<WeeklyMenuTemplate> findByActiveTrue();
}