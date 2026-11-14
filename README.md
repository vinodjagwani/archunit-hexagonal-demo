# archunit-hexagonal-demo

A tiny Spring Boot order service whose hexagonal architecture is **enforced by tests**, using
[ArchUnit](https://www.archunit.org/) and its built-in
[`onionArchitecture()`](https://www.archunit.org/userguide/html/000_Index.html#_architectures) rule.

Stack: Java 25 · Spring Boot 4.1.2 · JUnit 6 · ArchUnit 1.5.1 · Maven 3.9.16 (via the Maven Wrapper)

## Run

```bash
./mvnw verify
```

11 tests: the onion rule and 1 rule of our own on the real code, 5 tests proving the rules catch broken
code, 4 behaviour tests.

The build targets Java 25. On an older JDK (17 or newer) pass your version, for example
`./mvnw verify -Djava.version=17`.

## Package layout

```
com.example.orders
├── OrdersApplication
├── domain
│   ├── model          Order, InvalidOrderException
│   └── service        PlaceOrder (driving port), OrderService (business logic), OrderRepository (driven port)
├── application        OrderingConfiguration (wiring)
└── adapter
    ├── rest           OrderController, PlaceOrderRequest, OrderResponse (driving adapter)
    └── persistence    InMemoryOrderRepository (driven adapter)
```

The layout follows the Onion Architecture example in the ArchUnit user guide. The rule in
`HexagonalArchitectureTest` maps each package to its role:

```java
onionArchitecture()
        .domainModels("..domain.model..")
        .domainServices("..domain.service..")
        .applicationServices("..application..")
        .adapter("rest", "..adapter.rest..")
        .adapter("persistence", "..adapter.persistence..");
```

## What the onion rule checks

- The domain must not depend on the application layer or on adapters.
- The application layer may use the domain, but must not depend on adapters.
- Adapters may use the domain and the application layer.
- Adapters must not depend on each other.

The rule also rejects a class outside the mapped packages that uses one of the layers. That is why the
wiring class lives in `application` and not in the root package.

## Tests worth reading

| Test | What it shows |
| --- | --- |
| `architecture/HexagonalArchitectureTest` | The onion rule, exactly as shown in the article |
| `architecture/DomainAllowListTest` | One stricter rule of our own: the domain may use only itself and `java..` |
| `architecture/ArchitectureRulesCatchViolationsTest` | Each rule fails on a deliberately broken example in `src/test/java/com/example/violations` |
| `domain/service/OrderServiceTest` | Business logic tested with a lambda instead of a database |
| `adapter/rest/OrderControllerTest` | Full wiring through HTTP |

## Why the extra rule

The onion rule checks only dependencies between its own layers. A framework annotation in the domain,
such as Spring's `@Service`, passes it. The allow-list rule in `DomainAllowListTest` catches that. To
allow a library in the domain, add its package to the rule.

## Try breaking it

Make the constructor of `OrderService` create an `InMemoryOrderRepository` instead of using the one it is
given, then run `./mvnw test`. The onion rule fails and names the class, the dependency and the line.

Or add `@org.springframework.stereotype.Service` to `OrderService`. The onion rule still passes, and the
allow-list rule fails.
