package com.infyniteloop.runningroom.kitchen.repository;

import com.infyniteloop.runningroom.kitchen.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository  extends JpaRepository<OrderItem, UUID> {
}
