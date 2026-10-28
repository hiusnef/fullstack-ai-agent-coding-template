---
name: add-page
description: Add a Next.js page or UI feature in frontend/ that uses the backend API. Use when asked to build a screen, page, form or component.
---
# Add a frontend page

1. Clarify the route, what the user sees and does, which API operations it needs, and the error / empty / loading states.
2. If the API does not exist yet, run the `add-endpoint` skill first (contract first).
3. `pnpm --dir frontend gen:api` so `src/lib/api/schema.d.ts` matches `api.yaml`.
4. Create the route in `frontend/src/app/<route>/page.tsx` as a Server Component.
5. Put interactive parts in `frontend/src/components/<name>.tsx` with `"use client"`; call the API only via
   `@/lib/api/client` (follow `greeting-form.tsx`). Handle `data` and `error`.
6. Write `<name>.test.tsx` next to the component: mock `@/lib/api/client`, cover success and one error.
7. Run `pnpm --dir frontend lint`, `typecheck`, `test`; fix failures.
8. Summarise: route, components, API operations used, what was not verified in a browser.
