# Roadmap

Phased, **scoped** delivery: production-grade **Angular full mockup** first, then **spec-first DDD backend** with **Docker seed data** and **Postman** collection. Details: [FRONTEND_ARCHITECTURE.md](FRONTEND_ARCHITECTURE.md), [BACKEND_ARCHITECTURE.md](BACKEND_ARCHITECTURE.md).

---

## Phase 0 — Documentation

- [x] Design spec, stack, Cognito ADR, conventions, Cursor rules
- [x] Frontend + backend architecture docs, UI-kit + DDD ADRs
- [x] **Stack accepted** — ADR-0001 through ADR-0006 (see [TECH_STACK.md](TECH_STACK.md))

---

## Phase 1 — Angular foundation (production scaffold)

**Goal:** Empty but **correct** structure — no feature mockup yet.

| Deliverable | Notes |
|-------------|--------|
| Angular CLI workspace | Strict TS, standalone, `src/styles/` global tokens only |
| `shell/` | `FixedNavigationComponent`, `TopBarComponent`, `AppShellComponent` — **separate** components |
| Design system | `app-button`, `app-modal`, `app-dashboard-card` in `shared/ui/` |
| UI kit module | Material adapter in `shared/ui-kit/material/`; swap point documented |
| `ModalService` | Single modal API; footer uses `app-button` only |
| Data-access pattern | Repository interfaces + `environment.useMockData` flag |
| Lint / format | ESLint, Prettier, `stylelint` optional for SCSS BEM-like sections |
| Placeholder routes | Shell + empty `router-outlet` |

**Exit criteria:** `ng build` passes; shell renders; opening a test modal uses standard layout; **no** overview widgets yet.

---

## Phase 2 — Full dashboard mockup (Angular only)

**Goal:** Pixel-faithful **full mockup** matching [DESIGN.md](DESIGN.md) / `dashboard.webp` using **only components** — **no random HTML**, no backend.

| Deliverable | Notes |
|-------------|--------|
| `features/overview/` | Page container + six **scoped** section components (see below) |
| **Total budgets** | `total-budgets/` — SCSS root `.total-budgets { }` only |
| **Spending this month** | `spending-this-month/` — chart + scoped `.spending-this-month` |
| **Goals** | `goals-widget/` — scoped `.goals-widget` |
| **Transactions** | `transactions-widget/` — scoped `.transactions-widget` |
| **Investments** | `investments-widget/` — scoped `.investments-widget` |
| **Recurring** | `recurring-widget/` — scoped `.recurring-widget` |
| Mock repositories | `overview-mock.repository.ts` + `*.mock.ts` data from DESIGN samples |
| `OverviewFacade` | Page uses facade + `async` pipe — **no** inline mock arrays in templates |
| Nav stubs | Other sidebar routes → `PlaceholderPageComponent` |
| Sidebar data | `FixedNavigationComponent` receives `navItems` config — no hardcoded nav in overview |

**Rules (enforced):**

- No Material/Prime imports in `features/` or `shell/`.
- No feature-specific rules in `src/styles/` except tokens/utilities.
- `ViewEncapsulation.Emulated` (default) on all section components.

**Exit criteria:** `ng serve` shows complete dashboard mockup; toggling `useMockData` still works; modal demo uses `AppModalComponent`; styles from one section do not break another (visual + selector review).

---

## Phase 3 — API specification & Docker mock data

**Goal:** Contract and database ready **before** backend Java code.

| Deliverable | Notes |
|-------------|--------|
| `docs/api/openapi.yaml` | All Phase-4 endpoints: health, me, dashboard/overview, later CRUD stubs |
| `docs/api/seed-users.md` | Test `user_sub` values matching seed SQL |
| `docker-compose.yml` | Postgres 16 |
| `docker/postgres/init/` | Schema baseline |
| `docker/postgres/seed/` | Mock data aligned with DESIGN + multi-user `user_sub` |
| Postman | Generate `docs/api/postman/finance-platform.postman_collection.json` + local environment |
| README section | `docker compose up`, import Postman, hit health |

**Exit criteria:** DB seeds successfully; OpenAPI validates; Postman collection runs against **mock server** or documented static examples; **no** Spring code required yet.

---

## Phase 4 — Backend: multi-module scaffold + incremental services

**Goal:** **Maven parent** + shared libs + **separate Spring Boot module per bounded context** — not one monolith. See [BACKEND_ARCHITECTURE.md](BACKEND_ARCHITECTURE.md), [adr/0007](adr/0007-modular-backend-services.md).

| Order | Module | Spec file | Deliverable |
|-------|--------|-----------|-------------|
| 4a | `platform-parent`, `platform-common`, `platform-security` | — | Parent POM, Money, JWT autoconfig |
| 4b | `dashboard-bff` | `docs/api/dashboard.openapi.yaml` | `:8080` health, me, stub overview |
| 4c | `budget-service` | `docs/api/budget.openapi.yaml` | `:8081` budgets schema + seed + APIs |
| 4d | BFF wiring | dashboard spec | BFF composes overview via HTTP to budget-service |
| 4e+ | `activity-log-service`, `goals-service`, … | per-service openapi | **New module per context** when UI needs it |

**Activity Log:** only in `activity-log-service` (`:8082`, [activity-log.openapi.yaml](api/activity-log.openapi.yaml)) — never merged into BFF or budget module code.

Each slice PR: **service openapi → implement in that module only → Flyway for that schema → Postman folder → BFF proxy if UI needs it**.

**Exit criteria:** `docker compose up` + `./mvnw -pl dashboard-bff,budget-service verify`; Postman folders **Dashboard BFF** and **Budget** pass.

---

## Phase 5 — Wire Angular to real API

**Goal:** Replace mock repositories with HTTP implementations — **no** template changes in section components.

| Deliverable | Notes |
|-------------|--------|
| `overview-http.repository.ts` | Implements same interface as mock |
| `providers.ts` | `useMockData: false` in environment.prod |
| Error/loading | Facade exposes `loading$`, `error$` |
| CORS / proxy | `proxy.conf.json` for local dev |

**Exit criteria:** Overview identical visually; data from API + Docker seed.

---

## Phase 6 — AWS Cognito auth

**Goal:** Protect API and routes; remove dev-only headers in prod.

| Deliverable | Notes |
|-------------|--------|
| Cognito User Pool + SPA client | PKCE |
| Angular | `authGuard`, `authInterceptor` |
| Spring | OAuth2 Resource Server |
| Postman | Bearer token from Cognito login |
| Seed users | Map Cognito `sub` to seed `user_sub` |

**Exit criteria:** 401 without token; User A cannot see User B data in Postman and UI.

---

## Phase 7 — Remaining nav pages & backend modules

- Each major nav area: Angular scoped feature + matching `*-service` module (if not already present).
- **Activity Log page** → `activity-log-service` only.
- Goals → `goals-service`; Portfolio → `portfolio-service`; etc.
- BFF updated only to aggregate/proxy — no domain logic in BFF.

---

## Phase 8 — Integrations & production

- CSV import, optional bank API (new ADR).
- Customize panel, Smart Tips.
- CI: OpenAPI diff, Postman publish optional, `ng build`, `./mvnw verify`.

---

## What “done” means for the mockup (Phase 2)

| In | Out |
|----|-----|
| All 6 overview sections as isolated components | Backend calls |
| Fixed nav + top bar as `shell/` components | Cognito |
| Global tokens in `src/styles/` | Ad-hoc HTML mockup |
| `app-modal` / `app-button` for any dialog | Per-feature Material dialogs |
| Swappable `ui-kit` | Second UI library impl (until needed) |

---

## Success metrics

- Section CSS does not regress sibling widgets when changing one feature.
- OpenAPI and Postman stay in sync (regenerate on spec change).
- Backend PRs include domain tests before controller merge.
- Switching `OverviewRepository` implementation does not change overview templates.
