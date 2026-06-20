# Tech stack

All choices are ADR-backed. Changes require a new or updated ADR.

---

## Summary

| Layer | Choice | Version |
|-------|--------|---------|
| Frontend | Angular + CLI | 19+, TS 5.x |
| UI library | Angular Material + SCSS | Material 19+ |
| Charts | Inline SVG | — |
| Auth | AWS Cognito User Pool (PKCE) | — |
| Auth SPA | `angular-oauth2-oidc` | — |
| Backend | Spring Boot + OAuth2 Resource Server | 3.4+, Java 21 |
| API | REST + OpenAPI 3.1 | — |
| Database | PostgreSQL 16+ | — |
| Migrations | Flyway (via Spring) | — |
| Build | Maven multi-module (`./mvnw`) | — |
| Local infra | Docker Compose | — |
| API testing | Postman (generated from OpenAPI) | — |

---

## Service ports

| Service | Port |
|---------|------|
| `dashboard-bff` | 8080 |
| `identity-service` | 8079 |
| `budget-service` | 8081 |
| `finance-service` | 8084 |
| `portfolio-service` | 8085 |

---

## AWS Cognito

User Pool + SPA app client (PKCE, no client secret). Groups `user` / `admin` → `cognito:groups` in JWT.
See `docs/adr/0004-auth-cognito.md`.

---

## Environment variables

| Variable | Purpose |
|----------|---------|
| `DATABASE_URL` | JDBC URL |
| `COGNITO_REGION` | AWS region |
| `COGNITO_USER_POOL_ID` | User Pool id |
| `COGNITO_CLIENT_ID` | SPA app client (public) |
| `COGNITO_ISSUER_URI` | JWT issuer for resource server |
| `API_BASE_URL` | Spring API origin (frontend) |

Never commit real `.env` or AWS keys.

---

## Data phases

| Phase | Source |
|-------|--------|
| MVP | Seeded Postgres via Docker Compose |
| v2 | CSV import |
| v3 | Bank/stock API (new ADR per integration) |
| v4 | Agentic Python ingestion |
