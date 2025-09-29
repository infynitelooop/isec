package com.infyniteloop.runningroom.kitchen.repository;

import com.infyniteloop.runningroom.kitchen.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository  extends JpaRepository<Order, UUID> {
}
