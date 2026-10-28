---
description: Project overview, commands and working agreement for every agent
alwaysApply: true
---
# Project: sample-service

Fullstack app: **Spring Boot 4 / Java 25** backend as a **Gradle multi-module build: DDD bounded contexts x Clean Architecture layers**, and a
**Next.js 16 / React 19 / TypeScript** frontend in `frontend/` (pnpm). The OpenAPI contract `contract/api.yaml` is the
single source for the API; the backend implements it, the frontend generates its types from it.

## Commands
Backend (repo root, Gradle wrapper):
- Everything (compile, tests, ArchUnit architecture tests, Spotless check): `./gradlew check`
- One module: `./gradlew :sample-service-contexts-greeting-application:test` · one test:
  `./gradlew :sample-service-contexts-greeting-adapter-web:test --tests '*GreetingControllerTest'`
- Format: `./gradlew spotlessApply`
- Run: `./gradlew :sample-service-bootstrap:bootRun --args='--spring.profiles.active=local'` (port 8080)

Frontend (`pnpm --dir frontend <script>` from the root):
- Regenerate API types after changing `contract/api.yaml`: `gen:api`
- `lint` · `typecheck` · `test` · `build` · `dev` (port 3000, proxies `/api/*` to the backend)

## Working agreement
- Read the relevant code and `docs/architecture.md` before changing anything non-trivial.
- For changes touching more than 3 files, write a short plan first and wait for approval.
- Keep diffs small and focused; do not reformat unrelated code.
- Every behaviour change ships with a test. Run the checks of each stack you touched before saying you are done.
- If a requirement is ambiguous, ask instead of guessing.
- Record significant design decisions as an ADR in `docs/adr/`.

## Repository map
- `modules/contexts/<ctx>/{domain,application,adapter/*}` one bounded context per folder · `modules/shared/{kernel,web}`
  · `modules/bootstrap`. Folder `modules/<a>/<b>` = module `:sample-service-<a>-<b>`, auto-discovered
- `build-logic/` Gradle convention plugins · `gradle/libs.versions.toml` all dependency versions
- `contract/api.yaml` API contract
- `frontend/src/app/` routes · `frontend/src/components/` UI · `frontend/src/lib/api/` API client
- `docs/` architecture notes and ADRs · `docs/ai/` guides for humans working with agents
- `.ai/` single source of truth for AI-agent config (edit here, then run `python scripts/sync_agents.py`)
