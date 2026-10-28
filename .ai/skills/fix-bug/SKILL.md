---
name: fix-bug
description: Fix a reported bug by first reproducing it with a failing test. Use when asked to fix a bug, an error, a stack trace or a failing behaviour.
---
# Fix a bug

1. Restate the bug: expected vs actual behaviour, input that triggers it. Ask if you cannot reproduce from the report.
2. Locate the cause: read the stack trace and the code path; do not guess-edit.
3. Write a failing test in the innermost module where the bug lives (domain > application > adapter; follow `.ai/rules/20-testing.md`). Run it and confirm it fails for the right reason.
4. Make the smallest change that makes the test pass. Fix the cause, not the symptom.
5. Run `./gradlew check`; all tests (including ArchUnit) must pass. Do not weaken existing tests.
6. Look for the same pattern elsewhere and list other places (fix only if asked).
7. Summarise: root cause, fix, the regression test name, related risks.
