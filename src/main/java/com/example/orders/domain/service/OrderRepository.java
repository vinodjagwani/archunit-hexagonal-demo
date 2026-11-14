package com.example.orders.domain.service;

import com.example.orders.domain.model.Order;

/** Driven (outbound) port: what the domain needs from the outside world. */
public interface OrderRepository {

    Order save(Order order);
}
