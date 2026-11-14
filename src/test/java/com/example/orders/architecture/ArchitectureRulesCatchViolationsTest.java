package com.example.orders.architecture;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;
import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A rule that never fails protects nothing. Each test imports a deliberately broken example from
 * {@code com.example.violations} (test sources only) and proves the rule reports it.
 */
class ArchitectureRulesCatchViolationsTest {

    private static final String VIOLATIONS_PACKAGE = "com.example.violations.";

    @Test
    @DisplayName("Onion rule rejects a domain model that uses a domain service")
    void testOnionRuleRejectsDomainModelUsingDomainService() {
        final JavaClasses classes = importScenario("modelusesservice");

        final EvaluationResult result = anOnionRule().evaluate(classes);

        assertThat(result.getFailureReport().getDetails())
                .anyMatch(detail -> detail.contains("Invoice") && detail.contains("TaxCalculator"));
    }

    @Test
    @DisplayName("Onion rule rejects a domain service that uses an adapter")
    void testOnionRuleRejectsDomainUsingAdapter() {
        final JavaClasses classes = importScenario("domainusesadapter");

        final EvaluationResult result = anOnionRule().evaluate(classes);

        assertThat(result.getFailureReport().getDetails())
                .anyMatch(detail -> detail.contains("LeakyBookingService") && detail.contains("InMemoryTicketStore"));
    }

    @Test
    @DisplayName("Onion rule rejects a domain service that uses the application layer")
    void testOnionRuleRejectsDomainUsingApplication() {
        final JavaClasses classes = importScenario("domainusesapplication");

        final EvaluationResult result = anOnionRule().evaluate(classes);

        assertThat(result.getFailureReport().getDetails())
                .anyMatch(detail -> detail.contains("PricingService") && detail.contains("DiscountSettings"));
    }

    @Test
    @DisplayName("Onion rule rejects an application class that uses an adapter")
    void testOnionRuleRejectsApplicationUsingAdapter() {
        final JavaClasses classes = importScenario("applicationusesadapter");

        final EvaluationResult result = anOnionRule().evaluate(classes);

        assertThat(result.getFailureReport().getDetails())
                .anyMatch(detail -> detail.contains("CheckoutUseCase") && detail.contains("SqlOrderStore"));
    }

    @Test
    @DisplayName("Onion rule rejects one adapter that uses another adapter")
    void testOnionRuleRejectsCoupledAdapters() {
        final JavaClasses classes = importScenario("adaptercoupling");

        final EvaluationResult result = anOnionRule().evaluate(classes);

        assertThat(result.getFailureReport().getDetails())
                .anyMatch(detail -> detail.contains("OrderEndpoint") && detail.contains("OrderTable"));
    }

    @Test
    @DisplayName("Onion rule misses a Spring annotation in the domain, the allow-list rule catches it")
    void testAllowListRuleCatchesFrameworkLeakThatOnionRuleMisses() {
        final JavaClasses classes = importScenario("frameworkleak");

        final EvaluationResult onionResult = anOnionRule().evaluate(classes);
        final EvaluationResult allowListResult = DomainAllowListTest.domainDependsOnlyOnItselfAndJava.evaluate(classes);

        assertThat(onionResult.hasViolation()).isFalse();
        assertThat(allowListResult.getFailureReport().getDetails())
                .anyMatch(detail -> detail.contains("org.springframework.stereotype.Service"));
    }

    private static JavaClasses importScenario(final String scenario) {
        return new ClassFileImporter().importPackages(VIOLATIONS_PACKAGE + scenario);
    }

    /**
     * The same layers as {@link HexagonalArchitectureTest}. Layers are optional here because each broken example
     * contains only the packages it needs.
     */
    private static ArchRule anOnionRule() {
        return onionArchitecture()
                .domainModels("..domain.model..")
                .domainServices("..domain.service..")
                .applicationServices("..application..")
                .adapter("rest", "..adapter.rest..")
                .adapter("persistence", "..adapter.persistence..")
                .withOptionalLayers(true);
    }
}
