# ADR 0009: Company namespace `io.hiusnef`

- Status: accepted
- Date: 2026-10-28

## Context
The scaffold used the placeholder namespace `com.example` for Java packages and `https://example.com/problems/` for
RFC 9457 problem `type` URIs. The company domain is `hiusnef.io`.

## Decision
- Base Java package: `io.hiusnef.sampleservice` (reverse domain + application name), e.g.
  `io.hiusnef.sampleservice.greeting.domain.Guest`. Gradle `group = "io.hiusnef"` (set in `java-conventions`).
- Problem `type` URIs: `https://hiusnef.io/problems/<error-code-slug>` (e.g. `.../problems/not-found`).
- Module names (`:sample-service-...`), folder layout and architecture rules are unchanged.

## Alternatives considered
- `io.hiusnef` without the application segment - shorter, but collides once the company has more than one service.

## Consequences
- Problem `type` values changed before any external client depended on them; from now on they are part of the API.
- ADRs 0001-0008 still mention `com.example`; they describe the past and are not rewritten.
