package com.example.orders.adapter.persistence;

import com.example.orders.domain.model.Order;
import com.example.orders.domain.service.OrderRepository;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/** Driven adapter: implements the OrderRepository port. Swap for JPA/JDBC without touching the domain. */
@Repository
public final class InMemoryOrderRepository implements OrderRepository {

    private final Map<UUID, Order> orders = new ConcurrentHashMap<>();

    @Override
    public Order save(final Order order) {
        orders.put(order.id(), order);
        return order;
    }
}
