package com.example.violations.adaptercoupling.adapter.rest;

import com.example.violations.adaptercoupling.adapter.persistence.OrderTable;

/** VIOLATION: the REST adapter talks to the persistence adapter directly. */
public final class OrderEndpoint {

    private final OrderTable orderTable = new OrderTable();

    public int count() {
        return orderTable.count();
    }
}
