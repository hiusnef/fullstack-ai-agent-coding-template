# ADR 0006: Backend modules under `modules/`, named after their path

- Status: accepted
- Date: 2026-10-28
- Refines: [ADR 0005](0005-clean-architecture-gradle-multi-module.md) (module layout only; layers and rules unchanged)

## Context
ADR 0005 put five Gradle modules at the repository root next to `frontend/`, `docs/`, `contract/`... The root got
crowded and adding a module meant editing `settings.gradle.kts`. Module names (`application`, `domain`) were also
too generic to recognise in jars, logs and IDEs.

## Decision
- Every backend module lives under `modules/`, grouped by layer: `core/domain`, `core/application`,
  `adapter/web`, `adapter/persistence`, `bootstrap`.
- Naming convention: folder `modules/<parent1>/.../<child>` -> Gradle module `:<rootProject>-<parent1>-...-<child>`,
  e.g. `modules/adapter/web` -> `:sample-service-adapter-web`.
- `settings.gradle.kts` discovers modules: any folder under `modules/` with a `build.gradle.kts` (skipping `build/`,
  `src/`) is included with that name. Creating the folder is enough to add a module.
- Java packages are unchanged (`com.example.sampleservice.domain`, `.application`, `.adapter.web`, ...), so
  `CleanArchitectureTest` is unchanged. The frontend stays in `frontend/` (not a Gradle module).

## Alternatives considered
- Flat `modules/<name>` - simpler, loses the visible core/adapter grouping.
- Per-feature modules (`modules/greeting/domain`...) - stronger isolation, too many modules for the current size.
- Explicit `include(...)` list - more visible, but every new module is a settings edit and a chance to forget.

## Consequences
- Module paths are longer in commands (`./gradlew :sample-service-adapter-web:test`).
- Any stray `build.gradle.kts` under `modules/` becomes a module; keep only real modules there.
- Verified when introduced: `./gradlew check` green (18 tests), all six architecture-violation probes still fail the
  build, and a new nested folder `modules/adapter/messaging/kafka` was picked up as
  `:sample-service-adapter-messaging-kafka`.
