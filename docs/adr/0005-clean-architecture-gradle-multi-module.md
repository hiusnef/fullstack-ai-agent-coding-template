# ADR 0005: Clean Architecture as Gradle modules, enforced by the build

- Status: accepted
- Date: 2026-10-28
- Supersedes: the backend layout part of ADR 0004 (single Maven module at the root, controller/service/repository)

## Context
A single module with `controller -> service -> repository` packages relies on discipline: nothing stops a controller
from using a repository or the domain from importing Spring. With AI agents writing much of the code, rules that are
only written down get broken quietly. We want the architecture to be checked by the build, and Gradle for faster,
more flexible multi-module builds.

## Decision
- Gradle (Kotlin DSL, wrapper 9.8.2) replaces Maven. Versions live in `gradle/libs.versions.toml`; shared build
  config in convention plugins under `build-logic/`. The Spring Boot BOM is imported as a platform.
- Backend modules follow Clean Architecture: `domain`, `application` (use cases + `port.in` / `port.out`),
  `adapter-web`, `adapter-persistence`, `bootstrap` (composition root). Dependencies point inward only.
- `domain` and `application` are framework-free; use cases are wired as beans in `bootstrap`.
- Enforcement in three layers: Gradle module dependencies (compile time), ArchUnit `CleanArchitectureTest` in
  `./gradlew check`, and agent rules (`.ai/rules/10-clean-architecture.md`, `add-endpoint` skill).
- The OpenAPI contract moves to `contract/api.yaml`, shared by the backend and the frontend.

## Alternatives considered
- Packages-only Clean Architecture in one module + ArchUnit - fewer modules, but wrong imports still compile.
- Spring Modulith - good for feature modules, not for layer purity of domain/application.
- Stay on Maven multi-module - works, but more XML and slower incremental builds for this layout.

## Consequences
- Wrong-direction imports fail to compile; naming and port rules fail `./gradlew check`.
- More files per feature (port interfaces, wiring); the `add-endpoint` skill lists the order.
- Each new external integration is a new adapter module or a new output port in an existing adapter.
- Verified when introduced: each of six deliberate violations (Spring in domain, adapter->adapter, adapter->use case
  class, class as input port, misnamed persistence class, application->adapter) made the build fail.
