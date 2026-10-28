# ADR 0002: Standard agent workflow and human handbook

- Status: accepted
- Date: 2026-10-28

## Context
Agents were configured (ADR 0001) but there was no shared way of working: how tasks are planned and verified,
when an agent must stop and ask, and how humans review AI-written code. Results varied by person and tool.

## Decision
- Every agent follows `.ai/rules/05-agent-workflow.md`: Explore -> Plan -> Implement -> Verify, with an explicit
  definition of done and stop-and-ask triggers.
- Recurring tasks are captured as skills (`add-endpoint`, `fix-bug`, `code-review`, `write-adr`).
- Claude Code permissions are authored in `.ai/permissions.json` instead of being hard-coded in the generator.
- Human guidance lives in `docs/ai/` (Vietnamese): getting started, handbook, prompt library, maintenance guide.
- PRs declare AI assistance and the author confirms they understand the full diff.

## Alternatives considered
- Per-tool guides only - duplicates content and drifts like the configs did before ADR 0001.
- Put all guidance in `AGENTS.md` - bloats the always-loaded context for every agent turn.

## Consequences
- `AGENTS.md` grows by one short section; detailed human guidance stays out of agent context.
- Workflow changes go through `.ai/rules/05-agent-workflow.md` and the handbook together.
