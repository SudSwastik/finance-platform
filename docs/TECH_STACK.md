# Tech stack

All ADRs accepted. Changes require a new or updated ADR.

---

## Summary

| Layer | Choice | Version |
|-------|--------|---------|
| Frontend | Angular + Angular CLI | 19+, TS 5.x |
| UI library | Angular Material + SCSS | Material 19+ |
| Charts | ng2-charts (Chart.js) | Chart.js 4.x |
| Auth | AWS Cognito User Pool (PKCE) | — |
| Auth SPA library | `angular-oauth2-oidc` | — |
| Backend | Spring Boot + Spring Security OAuth2 Resource Server | 3.4+, Java 21 |
| API | REST + OpenAPI 3.1 | — |
| Database | PostgreSQL 16+ | — |
| Migrations | Flyway (via Spring) | — |
| Build | Maven multi-module (`./mvnw`) | — |
| Local infra | Docker Compose (Postgres + LocalStack Cognito) | — |
| API testing | Postman (generated from OpenAPI) | — |

---

## Frontend

| Concern | Choice |
|---------|--------|
| Components | Standalone; OnPush where practical |
| Routing | Angular Router, lazy `loadComponent` / `loadChildren` |
| HTTP | `HttpClient` + `authInterceptor` (Bearer JWT) |
| State | RxJS `Observable` services |
| Styling | SCSS per component + `src/styles/_tokens.scss` |
| Modals | `AppModalComponent` + `ModalService` only |
| Data (dev) | Mock repositories via `environment.useMockData` |
| Testing | Karma/Jasmine |

---

## Backend

| Concern | Choice |
|---------|--------|
| Structure | Parent POM + `platform-common` + `platform-security` + `*-service` modules + `dashboard-bff` |
| Language | Java 21 |
| Framework | Spring Boot 3 (Web, Validation, Data JPA, Security) |
| BFF | `dashboard-bff` — Overview composition; WebClient to other services |
| Security | `platform-security` + OAuth2 Resource Server + `QueryContext` (scope-aware) |
| DB | Postgres; schema per service in dev |
| Multi-tenancy | `tenant_id` + `user_sub` on all tables; `QueryContext` enforces scope |
| DDD | Full layers inside each `*-service`: `domain → application → infrastructure → web` |

---

## Service ports (local dev)

| Service | Port |
|---------|------|
| `dashboard-bff` | 8080 |
| `identity-service` | 8079 |
| `budget-service` | 8081 |
| `activity-log-service` | 8082 |
| `goals-service` | 8083 |
| `finance-service` | 8084 |
| `portfolio-service` | 8085 |

---

## AWS Cognito

| Resource | Purpose |
|----------|---------|
| User Pool | Users, passwords, MFA optional |
| App client (SPA) | PKCE, no client secret in browser |
| Groups | `user`, `admin` → `cognito:groups` in JWT |

See `docs/adr/0004-auth-cognito.md`.

---

## Environment variables

| Variable | Module | Purpose |
|----------|--------|---------|
| `DATABASE_URL` | backend | JDBC URL |
| `COGNITO_REGION` | backend, frontend | AWS region |
| `COGNITO_USER_POOL_ID` | backend, frontend | User Pool id |
| `COGNITO_CLIENT_ID` | frontend | SPA app client (public) |
| `COGNITO_ISSUER_URI` | backend | JWT issuer for resource server |
| `API_BASE_URL` | frontend | Spring API origin |

Never commit real `.env` or AWS keys.

---

## Data phases

| Phase | Data source |
|-------|-------------|
| MVP | Seeded Postgres via Docker Compose |
| v2 | CSV import |
| v3 | Bank/stock API (new ADR per integration) |
| v4 | Agentic Python ingestion pipeline |
