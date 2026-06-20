# Seed users (Docker / local testing)

Used by `docker/postgres/seed/` and Postman. Map to Cognito `sub` in Phase 6.

| `user_sub` | Display name | Notes |
|------------|--------------|--------|
| `seed-user-alice` | Alice (test) | Default Postman / dev header |
| `seed-user-bob` | Bob (test) | Second user for isolation tests |

**Local dev (pre-Cognito):** send header `X-Dev-User-Sub: seed-user-alice` on protected endpoints (see [openapi.yaml](api/openapi.yaml)).

**Postman:** set `{{devUserSub}}` in environment.

After Cognito: create users whose `sub` matches these ids or update seed SQL to match real `sub` values.
