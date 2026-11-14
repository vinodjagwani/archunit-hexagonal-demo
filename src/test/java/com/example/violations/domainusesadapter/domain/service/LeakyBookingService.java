package com.example.violations.domainusesadapter.domain.service;

import com.example.violations.domainusesadapter.adapter.persistence.InMemoryTicketStore;

/** VIOLATION: a domain service creates an adapter instead of using an interface it owns. */
public final class LeakyBookingService {

    private final InMemoryTicketStore store = new InMemoryTicketStore();

    public void book(final String ticketId) {
        store.save(ticketId);
    }
}
