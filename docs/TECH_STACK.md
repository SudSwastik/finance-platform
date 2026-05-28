# Tech stack

**Status:** All stack ADRs **Accepted** — includes [0007](adr/0007-modular-backend-services.md) **multi-module backend** (separate Spring Boot per bounded context).

Single source of truth for tools and versions. Changes require a new or updated ADR.

---

## Summary

| Layer | Choice | Version (target) | Why |
|-------|--------|------------------|-----|
| Frontend | **Angular** + Angular CLI | Angular 19+, TS 5.x | User choice; structured SPA, guards, DI |
| UI | Angular Material + SCSS | Material 19+ | Native Angular components, theming |
| Charts | ng2-charts (Chart.js) | Chart.js 4.x | Line chart for spending comparison |
| Auth | **AWS Cognito** User Pool | — | Managed auth; JWT for SPA + API |
| Auth (SPA) | OIDC PKCE or Amplify Auth | — | Secure browser login flow |
| Backend | Spring Boot + Spring Security OAuth2 Resource Server | 3.4+, Java 21 | JWT validation against Cognito JWKS |
| API | REST + OpenAPI | OpenAPI 3.1 | Contract-first |
| Database | PostgreSQL | 16+ | Per-user data scoped by Cognito `sub` |
| Migrations | Flyway | via Spring | Versioned schema |
| Cloud (auth) | AWS Cognito | — | User directory, groups, MFA optional |
| Package managers | npm (frontend), Maven (backend) | — | Standard |
| Local infra | Docker Compose | — | Postgres + seed SQL for mock API testing |
| API contract | OpenAPI 3.1 | `docs/api/openapi.yaml` | Spec-first; drives Postman |
| API testing | Postman collection | generated | From OpenAPI on each spec change |
| Frontend arch | Scoped components + ui-kit | — | See FRONTEND_ARCHITECTURE.md |
| Backend arch | Multi-module DDD + spec-first | — | See BACKEND_ARCHITECTURE.md, ADR-0007 |

---

## Frontend (`frontend/`)

| Concern | Choice |
|---------|--------|
| Language | TypeScript (strict) |
| Workspace | Angular CLI (`ng new`) |
| Components | Standalone; OnPush where practical |
| Routing | Angular Router, lazy `loadComponent` / `loadChildren` |
| HTTP | `HttpClient` + `authInterceptor` (Bearer JWT) |
| State | RxJS `Observable` services; signals optional for local UI |
| Auth | Cognito via `angular-oauth2-oidc` or `@aws-amplify/ui-angular` |
| Guards | `authGuard` on all app routes except login/callback |
| Styling | SCSS per component (scoped) + global `src/styles/_tokens.scss` |
| UI modals | `AppModalComponent` + `ModalService` only |
| Data (Phase 2) | Mock repositories; Phase 5 HTTP |
| Testing | Karma/Jasmine; component tests per section |

**Scripts (after scaffold):**

```bash
cd frontend && npm install && ng serve
cd frontend && ng test
cd frontend && ng build
```

---

## Backend (`backend/` — Maven multi-module)

| Concern | Choice |
|---------|--------|
| Structure | Parent POM + `platform-common` + `platform-security` + `*-service` modules + `dashboard-bff` |
| Deployables | One Spring Boot jar **per bounded context** (e.g. `activity-log-service`) |
| Language | Java 21 |
| Framework | Spring Boot 3 (Web, Validation, Data JPA, Security) per module |
| BFF | `dashboard-bff` — Overview composition; WebClient to other services |
| Security | `platform-security` + OAuth2 Resource Server in each app |
| Build | Maven (`./mvnw -pl <module>`) |
| DB | Postgres; **schema per service** in dev |
| API spec | `docs/api/<service>.openapi.yaml` |
| DDD | Full layers **inside each** `*-service` module |
| Postman | Folder per service in `docs/api/postman/` |

**Scripts (after scaffold):**

```bash
cd backend && ./mvnw -pl dashboard-bff spring-boot:run
cd backend && ./mvnw -pl activity-log-service spring-boot:run
cd backend && ./mvnw test                    # all modules
cd backend && ./mvnw -pl budget-service test
```

---

## AWS Cognito

| Resource | Purpose |
|----------|---------|
| User Pool | Users, passwords, MFA |
| App client (SPA) | PKCE, allowed callback/logout URLs |
| Hosted UI (optional) | Login page hosted by AWS |
| Groups | `user`, `admin` → `cognito:groups` in JWT |

See [adr/0004-auth-cognito.md](adr/0004-auth-cognito.md) for flows and Spring config.

---

## Data & integrations (phased)

| Phase | Data source |
|-------|-------------|
| MVP | Seeded Postgres per Cognito `sub` (test users) |
| v2 | Manual CSV import |
| v3 | Bank aggregator — new ADR required |

---

## Environment variables

Document in `.env.example` (backend) and `frontend/src/environments/environment.example.ts`.

| Variable | Module | Purpose |
|----------|--------|---------|
| `DATABASE_URL` | backend | JDBC URL for Postgres |
| `COGNITO_REGION` | backend, frontend | AWS region |
| `COGNITO_USER_POOL_ID` | backend, frontend | User Pool id |
| `COGNITO_CLIENT_ID` | frontend | SPA app client id (public) |
| `COGNITO_ISSUER_URI` | backend | JWT issuer for resource server |
| `COGNITO_DOMAIN` | frontend | Hosted UI domain (if used) |
| `API_BASE_URL` | frontend | Spring API origin |

Never commit real `.env` or AWS access keys.

---

## Tooling (Phase 3+)

```bash
docker compose up -d
npx openapi-to-postmanv2 -s docs/api/openapi.yaml -o docs/api/postman/finance-platform.postman_collection.json
```

## Open decisions

- [ ] OIDC library vs Amplify for Cognito (Phase 6)
- [ ] ng2-charts vs Chart.js wrapper in `spending-this-month` component
- [ ] Springdoc-only Postman export vs openapi-to-postmanv2 in CI

When resolved, update this table and ADR status if needed.
