package com.example.orders.architecture;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

import com.example.orders.OrdersApplication;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(
        packagesOf = OrdersApplication.class,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalArchitectureTest {

    @ArchTest
    static final ArchRule hexagonalArchitecture = onionArchitecture()
            .domainModels("..domain.model..")
            .domainServices("..domain.service..")
            .applicationServices("..application..")
            .adapter("rest", "..adapter.rest..")
            .adapter("persistence", "..adapter.persistence..");
}
