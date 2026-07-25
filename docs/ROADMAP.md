# Roadmap

---

## Done ✅

### Phase 0 — Docs + ADRs
Design spec, stack, ADR-0002 through ADR-0007, conventions.

### Phase 1 — Angular scaffold
Shell (`FixedNavigationComponent`, `TopBarComponent`, `AppShellComponent`), `app-button`, `app-modal`, `app-dashboard-card`, ui-kit Material adapter, mock data pattern, placeholder routes.

### Phase 2 — Auth pages
`/login`, `/register`, onboarding shell (`AuthShellComponent`), `AuthFacade` stub, `authGuard` stub, password strength bar, cross-nav links.

### Phase 3 — Overview mockup
Overview page: `total-budgets`, `investments-widget`, `recurring-widget`. Nav: Overview, Wallets & Banks, Subscriptions, Portfolio.

### Phase 4 — Initial backend scaffold
`platform-common`, `platform-security`, `dashboard-bff`, `budget-service`, `ledger-service`, `portfolio-service`, `recurring-service`. Docker Compose + LocalStack Cognito + Flyway seed. OpenAPI specs. `X-Dev-User-Sub` local auth.

---

## Upcoming

### Phase 5 — Backend architecture refactor ✅
**Goal:** Replace initial scaffold with multi-tenant accounts-first data model.

| Deliverable | Notes |
|-------------|-------|
| `identity-service` :8079 | `identity` schema: `tenants`, `users`, `user_relationships` |
| `finance-service` :8084 | `finance` schema: `accounts`, `transactions`, `investment_transactions`, `assets`. Absorbs `ledger-service` + `recurring-service` |
| Delete `ledger-service`, `recurring-service` | Replaced by `finance-service` |
| `tenant_id` migration | Add to all existing schemas (`budget`, `portfolio`) |
| `platform-security` update | `QueryContext` with scope-aware filtering: `finance:own`, `finance:tenant`, `finance:platform` |
| OpenAPI update | Add `finance.openapi.yaml`; remove `ledger.openapi.yaml`, `recurring.openapi.yaml` |
| BFF update | Wire `identity-service` and `finance-service` clients |
| Postman regenerate | Reflect new service map |

**Exit criteria:** `docker compose up`; unified `/finance/transactions` returns bank + card + stock + crypto filtered by `user_sub`; two seed users isolated; `./mvnw verify` passes.

**Completed 2026-05-30.** `identity-service` + `finance-service` created. `ledger-service` + `recurring-service` deleted. `QueryContext` added to `platform-security`. BFF trimmed to 3 clients (budget, portfolio, finance). All 14 unit tests pass.

---

### Phase 6 — Wire Angular to API ✅
| Deliverable | Notes |
|-------------|-------|
| `overview-http.repository.ts` | HTTP impl, same interface as mock |
| `providers.ts` | `useMockData: false` in `environment.prod` |
| Loading + error states | Facade exposes `loading$`, `error$`; `overview.page.html` renders loading/error/data branches |
| `proxy.conf.js` | CORS proxy for local dev (actual file wired in `angular.json`; unused `proxy.conf.json` left in repo) |

**Completed 2026-07-20.** All overview widgets (totalBudgets, investments, recurring, net worth, monthly summary, recent transactions) wired to live BFF. Transactions page (stats strip + paginated table, `feature/transactions-page` branch) also wired to `finance-service` directly via proxy, ahead of Phase 8's dedicated nav pages.

**Exit criteria:** Overview identical visually (styling applied in `c99b0c7`); data from Docker seed via API — verified `GET /api/v1/dashboard/overview` and `GET /api/v1/finance/transactions` both return 200 with seeded data through the Angular dev server proxy.

---

### Phase 7 — Cognito auth + multi-tenant
| Deliverable | Notes |
|-------------|-------|
| `authGuard` real impl | Redirects unauthenticated to `/login` |
| `authInterceptor` | Attaches Bearer token; handles token refresh |
| `AuthFacade` real impl | Cognito PKCE via `angular-oauth2-oidc` |
| Spring config swap | `issuer-uri` → real Cognito endpoint (env var only, no code change) |
| Remove `X-Dev-User-Sub` | Strip from prod security config |
| JWT claims | `tenant_id`, `user_sub`, `scope` in token |

**Exit criteria:** Real user signs up → verifies email → logs in → sees seeded data; 401 without token; two users isolated.

---

### Phase 8 — Remaining nav pages ✅
| Page | Service | Notes |
|------|---------|-------|
| Subscriptions | `finance-service` | Recurring transactions (type = RECURRING) |
| Wallets & Banks | `finance-service` | Accounts list + per-account transaction view, computed balances |
| Portfolio | `portfolio-service` + `finance-service` | Holdings read model (+ assetType) + trade history (new Asset/InvestmentTransaction slice in finance-service) |

Each: Angular scoped feature + service endpoint + OpenAPI entry.

**Completed 2026-07-20.** Portfolio's value-over-time chart and daily "today" movers from the original design mockup were dropped — no historical price/daily-delta data exists anywhere in the schema, and fabricating it was rejected in favor of a page built entirely from real computed data (stat strip, Stocks/Crypto mix, holdings table, trade history).

---

### Phase 9 — Goals ✅

**Goal:** Build the savings-goals feature end-to-end. Previously it was only a sidebar link with no route (`/goals` silently redirected to `/overview`), a static design comp (`design/Goals.dc.html`), and a minimal read-only OpenAPI stub (`docs/api/goals.openapi.yaml` — `GET /api/v1/goals` only). No `goals-service` module existed and it wasn't registered in `backend/pom.xml` or `infra/local/docker-compose.yml`.

Working in small, independently-shippable steps:

| Step | Deliverable | Notes |
|------|-------------|-------|
| 9a | OpenAPI expansion | Add `createGoal`, `updateGoal`, `deleteGoal`, `contributeToGoal` to `goals.openapi.yaml`; define request/response schemas |
| 9b | `goals-service` backend | New DDD module (`goals` schema: `goals`, `goal_contributions`), Flyway migration, register in `backend/pom.xml` + `infra/local/docker-compose.yml`. Port per `CLAUDE.md` module table (verify against `docs/ARCHITECTURE.md` — tables currently disagree, reconcile while touching this) |
| 9c | Angular `/goals` page (mock) | `features/goals/` + `data-access/goals/` (`GoalsRepository` + mock impl), `Goal` model, route wired in `app.routes.ts`. Build to the `design/Goals.dc.html` comp: stat strip (Total saved, Combined progress, Monthly contributions) + goal cards (progress ring, current/target, "On track" badge, "Add funds") |
| 9d | Wire Angular to live API | `GoalsHttpRepository`, `useMockData` swap, loading/error states |
| 9e | Overview "Goals" widget | Per `docs/DESIGN.md`: 4-col widget, 3 goals with progress bars, "View all" link → `/goals` |

**Completed 2026-07-26.** `/goals` renders real data from `goals-service`; full create/edit/delete/contribute round-trips verified in-browser against the live backend into Postgres. Overview's goals widget fetches independently of the BFF (via `GoalRepository` in `OverviewFacade`, not `OverviewResponse`) so a `goals-service` outage only empties that widget — same BFF-bypass pattern as Wallets/Portfolio/Subscriptions/Transactions. `goals-service` is also the first write-path service in the codebase, establishing the `*Command`/`*CommandHandler` convention and its own validation/404 handling.

Along the way: found and fixed a pre-existing bug where `AppModalComponent`'s Confirm button silently rendered with no label (Material MDC content-projection quirk, never caught since no page had used `ModalService.open()` before Goals) by switching the modal footer to plain styled buttons; also fixed stale port numbers in `docs/REFERENCE.md`'s service table.

**Exit criteria:** `/goals` renders real data from `goals-service`; a goal can be created and contributed to end-to-end; Overview shows the goals summary widget. — **met.**

---

### Phase 10 — AWS infrastructure (Terraform)
EC2 + Nginx + real Cognito User Pool. Modules: `state/`, `ec2/`, `cognito/`, optional `dns/`.

**Exit criteria:** `terraform apply` provisions all; app reachable at Elastic IP; HTTPS works; LocalStack removed from prod compose.

---

### Phase 11 — Integrations
CSV import, bank/stock API pull (new ADR per integration), agentic Python ingestion pipeline (classify + seed DB).
