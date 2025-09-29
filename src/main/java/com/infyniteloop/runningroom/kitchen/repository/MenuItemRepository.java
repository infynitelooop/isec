package com.infyniteloop.runningroom.kitchen.repository;

import com.infyniteloop.runningroom.kitchen.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
}