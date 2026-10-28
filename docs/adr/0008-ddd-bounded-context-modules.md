# ADR 0008: Bounded contexts (DDD) as the top-level split, layers inside each context

- Status: accepted
- Date: 2026-10-28
- Supersedes: the `modules/core/*` + `modules/adapter/*` layout of ADR 0006 (naming convention unchanged);
  refines ADR 0007 (`shared` split into `shared/kernel` and `shared/web`)

## Context
Layer-first modules (`core/domain`, `core/application`, `adapter/web`...) mix every business area in the same modules.
As the app grows, unrelated features share a domain module and nothing stops one area from reaching into another.
We want the business structure (DDD bounded contexts) to be visible and enforced, keeping Clean Architecture inside.

## Decision
- Top-level split by bounded context: `modules/contexts/<ctx>/{domain,application,adapter/web,adapter/persistence}`
  -> `:sample-service-contexts-<ctx>-<layer>`; packages `com.example.sampleservice.<ctx>.<layer>`.
- `shared/kernel` (JDK only): errors with `ErrorCode`, DDD building blocks (`AggregateRoot`, `Entity`, `ValueObject`,
  `DomainEvent`, `DomainEventPublisher`), `Preconditions`. `shared/web` (Spring): `GlobalExceptionHandler`, used by the
  web adapters of every context. The handler moved out of the greeting web adapter because it is cross-context.
- Repository interfaces live in the domain (classic DDD); `port.out` remains for external systems.
- Domain events are recorded by aggregates, pulled by the use case after `save`, and published in-process as Spring
  application events by `SpringDomainEventPublisher` in `bootstrap`.
- Contexts talk only through `<ctx>.application.port.in` or domain events (ArchUnit `contextsTalkOnlyThroughInputPorts`).
- Greeting remodelled: `Guest` aggregate (counts greetings, records `GuestGreeted`), value objects `PersonName`,
  `GuestId`, `Greeting`, `GuestRepository` with an in-memory adapter seeded with Ada, Alan, Grace, Linus.
  Behaviour change: only registered guests are greeted; other valid names now return 404 (previously only "nobody").

## Alternatives considered
- One module per context with layers as packages - fewer modules, but wrong-layer imports compile (ArchUnit only).
- Repositories as application `port.out` - keeps the domain persistence-agnostic in name too, less idiomatic DDD.
- Message broker for events now - not needed in a single process; swap the publisher (outbox/broker) later.

## Consequences
- Four modules per context; the `add-context` skill scripts the setup. Module names get long.
- Verified when introduced: `./gradlew check` green (38 tests); probes fail the build for: another context importing a
  domain class or a use case class (allowed: its `port.in`), mutable value object, repository as a class, aggregate
  outside the domain, domain using `shared.web` or Spring, controller using a use case class.
