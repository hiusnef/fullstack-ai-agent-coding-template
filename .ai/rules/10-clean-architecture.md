---
description: Module layout (bounded contexts x Clean Architecture layers), dependency rule and where code goes
alwaysApply: true
---
# Backend architecture: bounded contexts x Clean Architecture
Each **bounded context** (DDD, see `.ai/rules/12-ddd.md`) is split into layer modules. Dependencies point **inward only**:
`adapter/* -> application -> domain -> shared/kernel`. `bootstrap` is the composition root.

Module naming: every folder `modules/<parent1>/.../<child>` with a `build.gradle.kts` is the Gradle module
`:sample-service-<parent1>-...-<child>` (auto-discovered). Reference modules with `project(":sample-service-...")`.

| Folder under `modules/` | Package (`io.hiusnef.sampleservice.`) | Contains | May depend on |
|---|---|---|---|
| `shared/kernel` | `shared.kernel.{error,ddd,util}` | `DomainException` + `ErrorCode`, DDD building blocks (`AggregateRoot`, `Entity`, `ValueObject`, `DomainEvent`, `DomainEventPublisher`), `Preconditions`; JDK only | nothing |
| `shared/web` | `shared.web.*` | Spring MVC pieces shared by web adapters (`GlobalExceptionHandler`) | `shared/kernel` |
| `contexts/<ctx>/domain` | `<ctx>.domain` | aggregates, entities, value objects, domain events, **repository interfaces** | `shared/kernel` |
| `contexts/<ctx>/application` | `<ctx>.application`, `.port.in`, `.port.out` | use cases; input ports (`*UseCase`, the context's public API); output ports for external systems | own `domain` |
| `contexts/<ctx>/adapter/web` | `<ctx>.adapter.web` | controllers, request/response DTOs | own `application` ports, `shared/web` |
| `contexts/<ctx>/adapter/persistence` | `<ctx>.adapter.persistence` | repository implementations (`*Adapter`), JPA entities later | own `domain` |
| `bootstrap` | root + `config` | `@SpringBootApplication`, `@Bean` wiring of use cases, event publisher, `application.yml` | everything |

Hard rules (enforced by Gradle module dependencies and `modules/bootstrap/.../CleanArchitectureTest`; never weaken them):
- `shared/kernel`, `domain` and `application` have **no framework code**: no Spring, no `jakarta.persistence`, no servlet.
  Use cases are plain classes registered as beans in `modules/bootstrap/.../config/UseCaseConfiguration`.
- Input ports live in `port.in`, are interfaces, end with `UseCase`. `port.out` (interfaces) is only for external
  systems (mail, other services); persistence goes through domain repositories.
- Adapters talk to the application **only through ports**; never inject a use case implementation class.
- Adapters never depend on each other; `shared/web` is for adapters only. Persistence classes end with `Adapter`;
  controllers end with `Controller` and live in `adapter.web`.
- Never return domain objects or JPA entities from controllers; map to DTOs in the web adapter.
- Errors: throw `shared.kernel.error` exceptions (`NotFoundException`, `ConflictException` or another `DomainException`
  with an `ErrorCode`); `shared.web.error.GlobalExceptionHandler` maps `ErrorCode` -> HTTP status and RFC 9457 `type`.
  New category = new `ErrorCode` constant (the exhaustive switch forces an HTTP mapping).
- `shared/*` is not a dumping ground: only code that is cross-cutting and used by 2+ contexts/modules. No business rules.
- Bean Validation belongs on DTOs / controller parameters; domain invariants are checked in domain constructors.
- New dependencies: version in `gradle/libs.versions.toml`, declared only in the module that needs it.
