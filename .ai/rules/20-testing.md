---
description: How to write tests in this repository
globs: ["**/src/test/**/*.java"]
---
# Testing rules
- Test class name = class under test + `Test` (unit) or `IT` (integration).
- Test where the code lives, one module at a time:
  - `shared/kernel`, `<ctx>/domain`: plain JUnit, no mocks. Test aggregate behaviour and the events it records.
  - `<ctx>/application`: JUnit + Mockito; mock the **repository** and `DomainEventPublisher`, use `Clock.fixed`;
    verify save + published events. Never mock the domain.
  - `<ctx>/adapter/web`: `@WebMvcTest` + `MockMvcTester`, mock the **input port** (`*UseCase`); a test-only
    `WebTestApplication` in the root package picks up `shared.web` too.
  - `<ctx>/adapter/persistence`: plain unit tests now; Testcontainers (never H2) once a real database is added.
  - `bootstrap`: `*IT` context tests for wiring (`@RecordApplicationEvents` for events), and `CleanArchitectureTest` (ArchUnit). Never relax an ArchUnit rule to make code pass.
- Name tests `should<Expected>_when<Condition>`; structure as given / when / then.
- One behaviour per test; assert with AssertJ.
- Do not weaken or delete an existing test to make a change pass; explain instead.
