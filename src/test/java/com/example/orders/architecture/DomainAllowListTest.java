package com.example.orders.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.example.orders.OrdersApplication;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * A stricter rule of our own, next to the onion rule. The onion rule only checks dependencies between its own
 * layers, so a framework annotation in the domain slips through. This allow-list closes that gap.
 */
@AnalyzeClasses(
        packagesOf = OrdersApplication.class,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class DomainAllowListTest {

    @ArchTest
    static final ArchRule domainDependsOnlyOnItselfAndJava = classes()
            .that().resideInAPackage("..domain..")
            .should().onlyDependOnClassesThat().resideInAnyPackage("..domain..", "java..")
            .because("the domain must stay plain Java; frameworks and helpers belong outside it");
}
