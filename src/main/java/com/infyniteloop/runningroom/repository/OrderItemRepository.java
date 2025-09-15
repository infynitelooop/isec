package com.infyniteloop.runningroom.repository;

import com.infyniteloop.runningroom.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository  extends JpaRepository<OrderItem, UUID> {
}
