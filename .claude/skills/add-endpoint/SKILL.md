---
name: add-endpoint
description: Add a new REST endpoint end to end in a bounded context through the Clean Architecture layers (contract, domain, use case, adapters, wiring, tests). Use when asked to create or expose a new API.
---
# Add a REST endpoint (DDD + Clean Architecture)

Reference slice: greeting context - `Guest` / `PersonName` / `GuestGreeted` / `GuestRepository` -> `GreetUseCase` /
`GreetService` -> `GreetingController` / `InMemoryGuestRepositoryAdapter` -> `UseCaseConfiguration`.
`<ctx>` below = `modules/contexts/<ctx>`.

1. Clarify method, path, request/response fields, auth scope and error cases. Ask if any are missing.
2. Pick the bounded context. If the behaviour belongs to none, stop and propose the `add-context` skill.
3. **Contract**: update `contract/api.yaml` first.
4. **Domain** (`<ctx>/domain`): put the rule in an aggregate behaviour method; new concepts as value objects (records);
   record a past-tense domain event; extend the repository interface if needed. Errors: `shared.kernel.error`
   exceptions (new category = new `ErrorCode`). Unit-test the aggregate and value objects.
5. **Application** (`<ctx>/application`): input port `port.in.<Verb><Noun>UseCase`; use case class: load -> behaviour
   -> save -> publish events. `port.out` only for external systems. Unit-test with mocked repository/publisher.
6. **Persistence** (`<ctx>/adapter/persistence`): implement new repository methods in the `*Adapter`. Test it.
7. **Web** (`<ctx>/adapter/web`): DTOs with Bean Validation, controller calling the input port, map domain -> DTO.
   `@WebMvcTest` mocking the use case: 2xx, 400 and one domain error.
8. **Wiring** (`modules/bootstrap/.../config/UseCaseConfiguration`): one `@Bean` per use case, typed as its input port.
9. Run `./gradlew check` (includes `CleanArchitectureTest`) and fix failures. Never relax an ArchUnit rule.
10. Frontend: `pnpm --dir frontend gen:api`, `pnpm --dir frontend typecheck`; continue with `add-page` if needed.
11. Summarise: files changed per module, the new contract, anything left open.
