package com.infyniteloop.runningroom.repository;

import com.infyniteloop.runningroom.model.MenuItem;
import com.infyniteloop.runningroom.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository  extends JpaRepository<Order, UUID> {
}
