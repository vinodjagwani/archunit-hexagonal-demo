package com.example.violations.modelusesservice.domain.service;

public final class TaxCalculator {
    public int calculate(final int subtotal) {
        return subtotal / 10;
    }
}
