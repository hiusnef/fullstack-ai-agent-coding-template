---
description: Security guardrails for agents
alwaysApply: true
---
# Security
- Never read, print or commit `.env*`, `*.pem`, `*.key`, or `application-prod*.yml` (only `.env.example`, which holds variable names without values, is committed).
- Secrets come from environment variables or the vault; never hard-code them, not even in tests.
- Validate every external input with Bean Validation (`@Valid`, constraints on DTOs).
- Use parameterised queries / Spring Data methods; no string-built SQL.
- Do not add a new dependency without saying why and checking its licence; versions go in `gradle/libs.versions.toml`.
- Do not run destructive commands (`git push --force`, `rm -rf`, DB migrations against shared envs) without explicit approval.
