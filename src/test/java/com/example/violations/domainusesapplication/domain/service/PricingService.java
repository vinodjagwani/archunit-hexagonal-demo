package com.example.violations.domainusesapplication.domain.service;

import com.example.violations.domainusesapplication.application.DiscountSettings;

/** VIOLATION: a domain service depends on the application layer. */
public final class PricingService {

    private final DiscountSettings discountSettings = new DiscountSettings();

    public int discounted(final int price) {
        return price - price * discountSettings.percent() / 100;
    }
}
