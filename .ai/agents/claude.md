## Claude Code specifics
- Use plan mode for any change touching more than 3 files.
- Skills live in `.claude/skills/` (generated from `.ai/skills/`); prefer them over ad-hoc steps.
- Scoped rules in `.claude/rules/` load automatically for matching paths.
