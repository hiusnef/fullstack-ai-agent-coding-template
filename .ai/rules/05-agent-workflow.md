---
description: How agents plan, execute, verify and report work
alwaysApply: true
---
# Agent workflow
Work in four steps: **Explore -> Plan -> Implement -> Verify**.

1. **Explore**: read the code you will touch, its tests and `docs/architecture.md`. Do not edit yet.
2. **Plan**: state the goal, the files to change and how you will verify. For more than 3 files, stop and wait for approval.
3. **Implement**: small steps; follow the matching skill in `.ai/skills/` when one exists.
4. **Verify**: run the build and the relevant tests; read the output, do not assume it passed.

## Definition of done
- Backend touched: `./gradlew check` passes (tests + ArchUnit + Spotless).
- Frontend touched: `pnpm --dir frontend lint`, `typecheck` and `test` pass.
- Every behaviour change has a test that fails without the change.
- No secrets, debug leftovers, commented-out code or unrelated reformatting in the diff.
- Docs / OpenAPI / ADR updated when behaviour, contract or design changed.

## Stop and ask when
- A requirement is ambiguous or two rules conflict.
- The fix needs a new dependency, a schema migration, or a change to a public API.
- The same test still fails after two honest fix attempts: report what you tried instead of looping.

## Reporting
End with: what changed (files), how it was verified (commands + result), and what is left open or unverified.
Never claim a step you did not run.
