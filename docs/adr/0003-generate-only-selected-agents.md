# ADR 0003: Generate config only for the agents the team uses

- Status: accepted
- Date: 2026-10-28

## Context
ADR 0001 generated native config for five agents. Only Claude Code is in use (a single developer), so the
repo carried five dot-folders of copies (`.agents`, `.codex`, `.cursor`, ...) that nobody read, cluttering the
tree, search results and PR diffs. Agents read fixed paths, so the folders cannot be merged into one.

## Decision
- `.ai/config.json` lists `targets`; the generator only writes files owned by those agents (`TARGETS` in
  `scripts/sync_agents.py`) plus `AGENTS.md`. Files of removed targets are deleted with their empty folders.
- Current targets: `claude` only (Copilot was briefly enabled, then dropped as unused).
- The generator also writes `.gitattributes` (`linguist-generated`) and, for Copilot/VS Code,
  `.vscode/settings.json` hiding pure-copy folders from Explorer and Search.
- `--stage` lets the pre-commit hook `git add` / `git rm` exactly the generated files.

## Alternatives considered
- Symlink copies to `.ai/` - removes duplicate content but not folders; breaks on Windows without `core.symlinks`.
- Keep all agents - zero setup when someone tries a new tool, but permanent clutter for an unused case.

## Consequences
- Trying another agent = add it to `targets` and run sync; nothing else to maintain.
- `.gitattributes` is generated (and `.vscode/settings.json` when `copilot` is enabled); add hand-written
  attributes via the generator, not by editing the file.
