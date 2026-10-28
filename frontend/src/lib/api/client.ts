import createClient from "openapi-fetch";
import type { paths } from "./schema";

// Types come from the backend contract (contract/api.yaml) via `pnpm gen:api`.
// Requests go to /api/* and are proxied to Spring Boot by next.config.ts.
export const api = createClient<paths>({ baseUrl: "/api" });
