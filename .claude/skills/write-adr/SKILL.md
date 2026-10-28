---
name: write-adr
description: Record an architecture decision as an ADR in docs/adr/. Use when a design choice is made or asked to document a decision.
---
# Write an ADR

1. Find the next number: highest `docs/adr/NNNN-*.md` + 1, zero-padded to 4 digits.
2. Copy `docs/adr/template.md` to `docs/adr/NNNN-short-kebab-title.md`.
3. Fill in Context (forces, constraints), Decision (one clear statement), Alternatives considered, Consequences (good and bad).
4. Status is `proposed` unless the user says the decision is accepted; date is today (YYYY-MM-DD).
5. If it supersedes an older ADR, set the old one's status to `superseded by NNNN` and link both ways.
6. Keep it under one page. Do not change code in the same step unless asked.
