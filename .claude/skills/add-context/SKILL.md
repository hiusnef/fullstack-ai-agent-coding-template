---
name: add-context
description: Create a new DDD bounded context as Gradle modules (domain, application, adapters) and wire it into bootstrap. Use when asked for a new business area, module or bounded context.
---
# Add a bounded context

1. Agree on the context name (lower-case business noun, e.g. `billing`), its responsibility in one sentence, its
   ubiquitous language (key terms) and how it relates to existing contexts (which `port.in` it calls, which events it
   reacts to). Record the decision with the `write-adr` skill if it changes context boundaries.
2. Create the modules by copying the greeting context's build files (they are discovered automatically):
   - `modules/contexts/<ctx>/domain/build.gradle.kts` -> `api(project(":sample-service-shared-kernel"))`
   - `modules/contexts/<ctx>/application/build.gradle.kts` -> `api(project(":sample-service-contexts-<ctx>-domain"))`
     (+ `implementation(project(":sample-service-contexts-<other>-application"))` only to call another context's `port.in`)
   - `modules/contexts/<ctx>/adapter/web` and `adapter/persistence` only when needed, same dependencies as greeting's.
3. Packages: `io.hiusnef.sampleservice.<ctx>.{domain,application,application.port.in,adapter.web,adapter.persistence}`.
   Web adapter tests need a `WebTestApplication` in the root package (copy greeting's).
4. Model the first aggregate, value objects, events and repository (`.ai/rules/12-ddd.md`), then add use cases with the
   `add-endpoint` skill.
5. Wire it in `modules/bootstrap/build.gradle.kts` (application + adapters) and `UseCaseConfiguration` (a section per context).
6. Run `./gradlew projects` (new modules listed as `:sample-service-contexts-<ctx>-...`) and `./gradlew check`.
7. Update `docs/architecture.md` (context list / map). Summarise the new modules and their public API (`port.in`).
