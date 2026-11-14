package com.example.orders.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.orders.domain.model.InvalidOrderException;
import com.example.orders.domain.model.Order;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    @Test
    @DisplayName("Placing an order saves it through the repository port and returns it")
    void testPlaceSavesOrder() {
        final List<Order> savedOrders = new ArrayList<>();
        final OrderService orderService = new OrderService(aRecordingRepository(savedOrders));

        final Order order = orderService.place("book", 2);

        assertThat(order).extracting(Order::product, Order::quantity).containsExactly("book", 2);
        assertThat(savedOrders).containsExactly(order);
    }

    @Test
    @DisplayName("Placing an order with a non-positive quantity is rejected and nothing is saved")
    void testPlaceRejectsNonPositiveQuantity() {
        final List<Order> savedOrders = new ArrayList<>();
        final OrderService orderService = new OrderService(aRecordingRepository(savedOrders));

        assertThatThrownBy(() -> orderService.place("book", 0))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("quantity must be positive");
        assertThat(savedOrders).isEmpty();
    }

    private static OrderRepository aRecordingRepository(final List<Order> savedOrders) {
        return order -> {
            savedOrders.add(order);
            return order;
        };
    }
}
