# ADR 0001: Single source of truth for AI-agent configuration

- Status: accepted
- Date: 2026-10-28

## Context
The team uses several coding agents (Claude Code, Codex, GitHub Copilot, Google Antigravity, Cursor).
Each reads instructions, skills and MCP config from different paths and formats. Hand-maintained
copies drift quickly.

## Decision
All agent config is authored once under `.ai/`. `scripts/sync_agents.py` renders each agent's native
files. `AGENTS.md` is the shared always-on instruction file; `CLAUDE.md` imports it with `@AGENTS.md`.
Generated files are committed so a fresh clone works without running anything; CI fails if they drift.

## Consequences
- Never edit generated files by hand (they carry a GENERATED header).
- Supporting a new agent = adding a renderer in the script.
- Agent file conventions change often; re-check vendor docs when upgrading tools.
