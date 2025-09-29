package com.infyniteloop.runningroom.kitchen.repository;

import com.infyniteloop.runningroom.kitchen.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, UUID> {
    Optional<Menu> findByMenuDate(LocalDate menuDate);
    boolean existsByMenuDate(LocalDate menuDate);
    List<Menu> findByMenuDateBetweenOrderByMenuDateAsc(LocalDate startDate, LocalDate endDate);
}