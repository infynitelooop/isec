package com.infyniteloop.runningroom.repository;

import com.infyniteloop.runningroom.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
}
