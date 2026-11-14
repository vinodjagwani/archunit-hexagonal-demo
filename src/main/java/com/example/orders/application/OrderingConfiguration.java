package com.example.orders.application;

import com.example.orders.domain.service.OrderRepository;
import com.example.orders.domain.service.OrderService;
import com.example.orders.domain.service.PlaceOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring: the only place that creates the domain service.
 * It lives in the application layer, so the domain stays free of Spring annotations.
 */
@Configuration
class OrderingConfiguration {

    @Bean
    PlaceOrder placeOrder(final OrderRepository orderRepository) {
        return new OrderService(orderRepository);
    }
}
