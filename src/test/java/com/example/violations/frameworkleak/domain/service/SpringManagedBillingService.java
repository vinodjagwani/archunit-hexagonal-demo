package com.example.violations.frameworkleak.domain.service;

import org.springframework.stereotype.Service;

/** VIOLATION: the domain is coupled to Spring. The onion rule alone does not notice. */
@Service
public final class SpringManagedBillingService {

    public String bill(final String invoiceId) {
        return invoiceId;
    }
}
