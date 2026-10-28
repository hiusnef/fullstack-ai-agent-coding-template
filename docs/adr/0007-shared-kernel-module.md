# ADR 0007: Shared kernel module for cross-cutting building blocks

- Status: accepted
- Date: 2026-10-28
- Refines: [ADR 0005](0005-clean-architecture-gradle-multi-module.md), [ADR 0006](0006-modules-folder-and-naming.md)

## Context
Error types and small helpers are needed by several modules (domain, application, adapters). Keeping them in
`core/domain` made adapters depend on the domain for non-domain concepts, and the web error handler hard-coded one
`@ExceptionHandler` and one `type` string per exception.

## Decision
- New module `modules/shared` -> `:sample-service-shared` (packages `shared.error`, `shared.util`).
- It is the innermost module: JDK only, no Spring, depends on no other project module; every module may use it
  (`core/domain` exposes it with `api`).
- `DomainException` carries a protocol-neutral `ErrorCode` (`NOT_FOUND`, `CONFLICT`) with a stable slug.
  `NotFoundException` / `ConflictException` moved from `domain.exception` to `shared.error`.
- `GlobalExceptionHandler` has one handler for `DomainException`; an exhaustive `switch` maps `ErrorCode` to HTTP
  status, the slug builds the RFC 9457 `type`. A new code without an HTTP mapping does not compile.
- `shared.util.Preconditions` replaces hand-written argument checks (first user: `Greeting`).
- Admission rule: cross-cutting, framework-free, used by 2+ modules. Otherwise it stays in its module.

## Alternatives considered
- Keep errors in `core/domain` - adapters then depend on the domain for plumbing; no place for non-domain helpers.
- Two modules (`shared/kernel` + `shared/web` with Spring helpers) - deferred until a second web/messaging adapter
  needs shared Spring code.
- Third-party utility libraries (Guava, Commons Lang) - not needed yet; would be added via the version catalog.

## Consequences
- `shared` can grow into a dumping ground; the admission rule is in `.ai/rules/10-clean-architecture.md` and reviews
  should reject feature-specific code there.
- Verified when introduced: `./gradlew check` green (22 tests); Spring in `shared`, `shared` importing the domain, and a
  `shared` package declared inside an adapter that calls the adapter all fail the build.
