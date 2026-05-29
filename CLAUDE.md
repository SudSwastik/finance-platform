# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

Personal finance dashboard. **Angular SPA** (full mockup phase first) + **Maven multi-module Spring Boot** backend (one deployable per bounded context) + **PostgreSQL** via Docker + **AWS Cognito** (Phase 6). Current status: backend scaffold done (phases 4a–4d); Angular mockup not yet started.

Read `docs/ROADMAP.md` for current phase checklist. Read `AGENTS.md` for task-to-doc routing.

---

## Commands

### Infrastructure

```bash
docker compose up -d          # start Postgres (required for backend)
```

### Backend (`backend/`)

```bash
cd backend && ./mvnw test                          # all modules
./mvnw -pl budget-service test                    # single module
./mvnw -pl budget-service spring-boot:run         # :8081
./mvnw -pl dashboard-bff spring-boot:run          # :8080
```

### Frontend (`frontend/`) — scaffold not yet created

```bash
cd frontend && npm install && ng serve            # dev server
cd frontend && ng test                            # Karma/Jasmine
cd frontend && ng build                           # production build
```

### Postman (regenerate after any OpenAPI change)

```bash
npx openapi-to-postmanv2 \
  -s docs/api/openapi.yaml \
  -o docs/api/postman/finance-platform.postman_collection.json
```

---

## Backend architecture

Maven multi-module monorepo under `backend/`. Each bounded context is a **separate Spring Boot deployable**, not a package inside one app (see `docs/adr/0007-modular-backend-services.md`).

| Module | Port | Role |
|--------|------|------|
| `platform-common` | jar | `Money`, `UserId`, `ErrorEnvelope` — no aggregates |
| `platform-security` | jar | JWT resource-server autoconfig (Cognito JWKS) |
| `dashboard-bff` | 8080 | Composes Overview; `/me`; `/health`; **no domain code** |
| `budget-service` | 8081 | Budgets / spending plan |
| `activity-log-service` | 8082 | Activity log only |
| `goals-service` | 8083 | Savings goals |
| `ledger-service` | 8084 | Transactions |
| `portfolio-service` | 8085 | Investments |
| `recurring-service` | 8086 | Subscriptions |

**DDD layers inside every `*-service`** (no cross-service domain imports):

```
domain → application → infrastructure → web
```

`domain` may only depend on `platform-common`. `application` uses domain ports. `infrastructure` implements ports with JPA. `web` (controllers) calls application handlers only — never repositories directly.

**Database**: one Postgres instance, **schema per service** (`budget`, `activity_log`, etc.). Each service uses `spring.datasource.url` with `?currentSchema=<schema>` and Flyway scoped to that schema.

**BFF pattern**: `dashboard-bff` calls domain services via `WebClient`; service URLs in config:

```yaml
platform:
  services:
    budget: http://localhost:8081
    activity-log: http://localhost:8082
```

**Spec-first workflow**: edit `docs/api/<service>.openapi.yaml` → implement inside that `*-service` only → update BFF spec if BFF exposes the route → regenerate Postman.

---

## Frontend architecture

Angular 19+, TypeScript strict, standalone components.

**Layer rules:**

| Layer | Rule |
|-------|------|
| `src/styles/` | Global tokens, reset, typography only — nothing feature-specific |
| `app/shell/` | `FixedNavigationComponent`, `TopBarComponent`, `AppShellComponent` — separate, stable; no feature SCSS |
| `app/shared/ui/` | `app-button`, `app-modal`, `app-dashboard-card` — library-agnostic primitives |
| `app/shared/ui-kit/` | Swappable adapter (Material today); features never import `@angular/material/*` directly |
| `app/features/overview/*/` | One scoped section per widget; styles under one root BEM class |
| `app/data-access/` | Repository interface + mock/HTTP swap via `environment.useMockData` |

**Scoped CSS** — every section component wraps all rules in one root class; no bare selectors at file root:

```scss
.total-budgets {
  &__header { }
  &__category-row { }
}
```

`ViewEncapsulation.None` on feature components requires an ADR.

**Modals**: always `ModalService.open()` + `AppModalComponent` — never open Material dialog directly in features.

**Mock data**: lives in `*-mock.repository.ts` / `*.mock.ts`; page components bind via `async` pipe through a facade — no inline arrays in templates.

---

## Hard rules (non-negotiable)

- **Money**: `BigDecimal` in Java; decimal strings in JSON. Never `float`/`double`.
- **Auth scoping**: use JWT `sub` only. Never trust a client-sent `userId`. Local dev: `X-Dev-User-Sub` header.
- **OpenAPI before endpoints**: no controllers without an existing `docs/api/<service>.openapi.yaml` entry.
- **No cross-context domain imports**: `budget-service` domain classes must not appear in `activity-log-service`.
- **No domain code in BFF**: `dashboard-bff` only composes HTTP responses from other services.
- **No UI library in features**: features import from `shared/ui` and `shared/ui-kit`, not `@angular/material/*` or PrimeNG.
- **Regenerate Postman** whenever any service OpenAPI changes.

---

## Naming conventions

| Item | Convention | Example |
|------|------------|---------|
| Section component | kebab folder + `.component.ts` | `total-budgets.component.ts` |
| SCSS root class | matches folder name | `.total-budgets` |
| Repository | interface + mock/http impl | `OverviewRepository`, `OverviewMockRepository` |
| OpenAPI `operationId` | camelCase | `getDashboardOverview` |
| Java aggregate | PascalCase | `BudgetCategory` |
| DB column / seed | snake_case | `user_sub` |
| Branch | `feature/…`, `fix/…`, `docs/…` | — |

---

## Testing

| Level | How |
|-------|-----|
| Angular section | `TestBed`; assert scoped root class present |
| Angular modal | assert `ModalService` opens `AppModalComponent` |
| Java domain/application | unit tests, no Spring context |
| Java API slice | `@WebMvcTest` per service |
| Java BFF contract | `MockWebServer` for downstream services |
| Integration | Testcontainers + same Flyway seed as Docker |

---

## Key docs

| Topic | File |
|-------|------|
| Current phase checklist | `docs/ROADMAP.md` |
| Frontend structure | `docs/FRONTEND_ARCHITECTURE.md` |
| Backend DDD + module map | `docs/BACKEND_ARCHITECTURE.md` |
| Naming / do-don't | `docs/CONVENTIONS.md` |
| Design / widget inventory | `docs/DESIGN.md` |
| Versions | `docs/TECH_STACK.md` |
| OpenAPI contracts | `docs/api/*.openapi.yaml` |
| Why multi-module | `docs/adr/0007-modular-backend-services.md` |
| Why Cognito | `docs/adr/0004-auth-cognito.md` |
| Seed test users | `docs/api/seed-users.md` |
