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

### Phase 6 — Wire Angular to API 🚧
| Deliverable | Notes |
|-------------|-------|
| `overview-http.repository.ts` | HTTP impl, same interface as mock |
| `providers.ts` | `useMockData: false` in `environment.prod` |
| Loading + error states | Facade exposes `loading$`, `error$` |
| `proxy.conf.json` | CORS proxy for local dev |

**In progress.** totalBudgets, investments, recurring widgets wired to live BFF.

**Exit criteria:** Overview identical visually; data from Docker seed via API.

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

### Phase 8 — Remaining nav pages
| Page | Service | Notes |
|------|---------|-------|
| Wallets & Banks | `finance-service` | Accounts list + per-account transaction view |
| Subscriptions | `finance-service` | Recurring transactions (type = RECURRING) |
| Portfolio | `portfolio-service` | Holdings read model + trade history |

Each: Angular scoped feature + service endpoint + OpenAPI entry.

---

### Phase 9 — AWS infrastructure (Terraform)
EC2 + Nginx + real Cognito User Pool. Modules: `state/`, `ec2/`, `cognito/`, optional `dns/`.

**Exit criteria:** `terraform apply` provisions all; app reachable at Elastic IP; HTTPS works; LocalStack removed from prod compose.

---

### Phase 10 — Integrations
CSV import, bank/stock API pull (new ADR per integration), agentic Python ingestion pipeline (classify + seed DB).
