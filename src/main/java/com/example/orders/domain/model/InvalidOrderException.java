package com.example.orders.domain.model;

public final class InvalidOrderException extends RuntimeException {

    public InvalidOrderException(final String message) {
        super(message);
    }
}
