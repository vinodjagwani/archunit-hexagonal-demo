package com.example.violations.applicationusesadapter.application;

import com.example.violations.applicationusesadapter.adapter.persistence.SqlOrderStore;

/** VIOLATION: the application layer depends on a concrete adapter. */
public final class CheckoutUseCase {

    private final SqlOrderStore store = new SqlOrderStore();

    public void checkout(final String orderId) {
        store.save(orderId);
    }
}
