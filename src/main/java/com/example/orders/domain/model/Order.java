package com.example.orders.domain.model;

import java.util.UUID;

public record Order(UUID id, String product, int quantity) {

    public Order {
        if (id == null) {
            throw new InvalidOrderException("id is required");
        }
        if (product == null || product.isBlank()) {
            throw new InvalidOrderException("product is required");
        }
        if (quantity <= 0) {
            throw new InvalidOrderException("quantity must be positive");
        }
    }
}
