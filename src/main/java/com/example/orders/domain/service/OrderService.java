package com.example.orders.domain.service;

import com.example.orders.domain.model.Order;
import java.util.UUID;

public final class OrderService implements PlaceOrder {

    private final OrderRepository orderRepository;

    public OrderService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order place(final String product, final int quantity) {
        final Order order = new Order(
                UUID.randomUUID(),
                product,
                quantity
        );

        return orderRepository.save(order);
    }
}
