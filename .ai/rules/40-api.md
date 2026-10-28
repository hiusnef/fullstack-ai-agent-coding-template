---
description: REST API design conventions
globs: ["modules/contexts/*/adapter/web/**", "modules/shared/web/**", "contract/**", "frontend/src/lib/api/**"]
---
# API conventions
- Contract first: update `contract/api.yaml` before the controller.
- After changing `contract/api.yaml`, run `pnpm --dir frontend gen:api` and `pnpm --dir frontend typecheck`; fix the frontend in the same change.
- Resource names are plural nouns, kebab-case: `/v1/payment-orders/{id}`.
- Version in the path (`/v1`); breaking changes need a new version.
- Errors use `application/problem+json` (RFC 9457) with a stable `type` URI.
- Collections are paginated (`page`, `size`, max 100) and return a `page` metadata object.
- Every endpoint documents its auth scope.
