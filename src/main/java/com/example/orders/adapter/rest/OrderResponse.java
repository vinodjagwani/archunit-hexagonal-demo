package com.example.orders.adapter.rest;

import java.util.UUID;

public record OrderResponse(UUID id, String product, int quantity) {
}
