---
name: code-review
description: Review the current diff or a merge request against project rules. Use when asked to review code, a branch, or an MR.
---
# Code review

1. Get the diff: `git diff --merge-base origin/main` (or the MR diff given by the user).
2. Check, in this order, and cite file:line for each finding:
   - Correctness and edge cases (nulls, empty collections, concurrency, transactions)
   - Security rules in `.ai/rules/30-security.md`
   - API rules in `.ai/rules/40-api.md` for controller/OpenAPI changes; contract and frontend client changed together
   - Tests: is each behaviour change covered? Are assertions meaningful?
   - Clean Architecture in `.ai/rules/10-clean-architecture.md`: dependency direction, ports, no framework in domain/application
   - DDD in `.ai/rules/12-ddd.md`: logic in aggregates (not use cases), value objects for rules-carrying primitives, context isolation
   - Style rules in `.ai/rules/11-java-style.md` (Java) and `.ai/rules/15-frontend.md` (frontend)
3. Classify each finding as **blocker**, **should-fix** or **nit**.
4. Output a short table of findings, then an overall verdict (approve / request changes).
Do not modify code during a review unless asked.
