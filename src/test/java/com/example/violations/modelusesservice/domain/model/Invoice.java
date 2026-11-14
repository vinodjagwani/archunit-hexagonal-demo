package com.example.violations.modelusesservice.domain.model;

import com.example.violations.modelusesservice.domain.service.TaxCalculator;

/** Deliberately invalid: a domain model should not depend on a domain service. */
public final class Invoice {
    private final TaxCalculator taxCalculator = new TaxCalculator();

    public int totalWithTax(final int subtotal) {
        return subtotal + taxCalculator.calculate(subtotal);
    }
}
