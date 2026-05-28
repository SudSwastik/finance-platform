# Environment configuration

## Backend (`.env` from repo root `.env.example`)

| Variable | Required | Description |
|----------|----------|-------------|
| `DATABASE_URL` | Yes | PostgreSQL JDBC URL |
| `COGNITO_REGION` | Yes | AWS region of User Pool |
| `COGNITO_USER_POOL_ID` | Yes | Cognito User Pool id |
| `COGNITO_ISSUER_URI` | Yes | `https://cognito-idp.{region}.amazonaws.com/{poolId}` |

## Frontend (`frontend/src/environments/environment.ts`)

Copy from `environment.example.ts` when scaffolded:

| Field | Required | Description |
|-------|----------|-------------|
| `apiBaseUrl` | Yes | e.g. `http://localhost:8080` |
| `cognito.region` | Yes | AWS region |
| `cognito.userPoolId` | Yes | User Pool id |
| `cognito.clientId` | Yes | SPA app client id (public) |
| `cognito.domain` | If Hosted UI | Cognito domain prefix |
| `cognito.redirectUri` | Yes | e.g. `http://localhost:4200/auth/callback` |
| `cognito.logoutUri` | Yes | e.g. `http://localhost:4200` |

## Cognito console checklist (Phase 1)

1. Create User Pool (email sign-in).
2. Create app client — **SPA**, authorization code grant, **PKCE**.
3. Set callback and sign-out URLs for local Angular dev.
4. (Optional) Create groups `user`, `admin`.
5. (Optional) Enable Hosted UI domain.

See [adr/0004-auth-cognito.md](adr/0004-auth-cognito.md).

## API testing (Postman)

- Import [api/postman/](api/postman/) after generating from [openapi.yaml](api/openapi.yaml).
- Local dev user: `X-Dev-User-Sub: seed-user-alice` per [seed-users.md](api/seed-users.md).
- Start DB: `docker compose up -d` (see [docker/README.md](../docker/README.md)).
