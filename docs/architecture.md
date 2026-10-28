# Architecture

> Agents read this before non-trivial changes. Keep it short and current.

## Context

`sample-service` is a fullstack app: a Next.js frontend for users and a Spring Boot REST backend.
Replace this paragraph with what the product does, who uses it and which external systems it calls.

```graph
Browser ──► Next.js (frontend/, :3000) ──/api/* rewrite──► Spring Boot (bootstrap, :8080)
               │                                              │
               └──── types generated from ◄── contract/api.yaml ──► implemented by modules/contexts/*/adapter/web
```

## Backend: DDD bounded contexts x Clean Architecture (Spring Boot 4.1 / Java 25, Gradle)
```
modules/
├── shared/kernel        JDK only: errors (ErrorCode), DDD building blocks, Preconditions   ◄── everyone
├── shared/web           Spring MVC: GlobalExceptionHandler (RFC 9457)                       ◄── web adapters
├── contexts/
│   └── greeting/        one bounded context
│       ├── domain             Guest (aggregate), PersonName/GuestId/Greeting (VOs), GuestGreeted (event), GuestRepository
│       ├── application        GreetUseCase (port.in = public API of the context), GreetService
│       ├── adapter/web        GreetingController ──► GreetUseCase
│       └── adapter/persistence InMemoryGuestRepositoryAdapter ──► GuestRepository
└── bootstrap            composition root: @SpringBootApplication, UseCaseConfiguration, SpringDomainEventPublisher
```
Base package `io.hiusnef.sampleservice` (`<ctx>.domain`, `<ctx>.application`, `shared.kernel`...); Gradle group `io.hiusnef`.
Inside a context dependencies point inward: `adapter/* -> application -> domain -> shared/kernel`.
Between contexts: only via the other context's `application.port.in` or its domain events.
Rules: `.ai/rules/10-clean-architecture.md`, `.ai/rules/12-ddd.md`.

Folder -> module: `modules/<parent1>/.../<child>` -> `:sample-service-<parent1>-...-<child>`, discovered by
`settings.gradle.kts` (e.g. `modules/contexts/greeting/adapter/web` -> `:sample-service-contexts-greeting-adapter-web`).

### Bounded contexts
| Context | Responsibility | Public API (`port.in`) | Events |
|---|---|---|---|
| `greeting` | Greets known guests and counts greetings | `GreetUseCase` | `GuestGreeted` |

### Request flow (greeting)
`GET /v1/greetings/{name}` -> `GreetingController` -> `GreetUseCase` (`GreetService`): load `Guest` via
`GuestRepository` -> `guest.greet(now)` (counts, records `GuestGreeted`) -> `save` -> `DomainEventPublisher`
(Spring application events, in-process) -> `GreetingResponse`. Unknown guest -> `NotFoundException` -> 404 problem.

### How the rules are enforced
1. **Gradle module dependencies** (compile time): kernel/domain/application have no Spring on the classpath; a context
   module only sees what its `build.gradle.kts` declares; adapters cannot see each other.
2. **ArchUnit** (`modules/bootstrap/src/test/.../CleanArchitectureTest`, part of `./gradlew check`): onion layers,
   framework-free core, ports-only adapters, naming, DDD building blocks (aggregates/events in domain, immutable value
   objects and events, repositories are domain interfaces), context isolation, no package cycles.
3. **Agent rules** in `.ai/rules/` and the `add-context` / `add-endpoint` skills.

### Build
- `build-logic/` holds convention plugins (`java-conventions`, `spring-app-conventions`); `gradle/libs.versions.toml`
  holds every version (Spring Boot BOM imported as a platform).
- Mockito runs as a `-javaagent` in tests. Configuration cache, build cache and parallel execution are on.

## Frontend (`frontend/`, Next.js 16 / React 19 / TypeScript, pnpm)

- `src/app/` App Router routes (Server Components by default)
- `src/components/` UI components; client components only where interactivity is needed
- `src/lib/api/client.ts` the only way to call the backend (openapi-fetch, base URL `/api`)
- `src/lib/api/schema.d.ts` generated from `contract/api.yaml` by `pnpm gen:api` (gitignored)

The browser calls `/api/*` on the Next.js origin; `next.config.ts` rewrites to `BACKEND_URL`
(default `http://localhost:8080`), so no CORS configuration is needed.

## Contract flow

1. Change `contract/api.yaml` (contract first, `.ai/rules/40-api.md`).
2. Aggregate behaviour -> use case -> adapters -> wiring (`add-endpoint` skill); `./gradlew check`.
3. `pnpm gen:api` -> frontend types update; `pnpm typecheck` shows every frontend call that broke.

## Key flows

Add one short section (or a diagram) per critical flow.

## Non-functional

Latency targets, idempotency, retry policy, observability, auth (not configured yet).
