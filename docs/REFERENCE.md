# Reference

Angular SPA · Multi-module Spring Boot (DDD) · PostgreSQL · AWS Cognito (Phase 7).
Design tokens: `CLAUDE.md` (source) · `docs/design-tokens.json` (machine-readable) · `design/*.dc.html` (mocks).

---

## Architecture

```
Angular SPA → dashboard-bff :8081 → WebClient (parallel) →
  identity:8082, goals:8083, budget:8084, finance:8085, portfolio:8086 → PostgreSQL (schema per service)
```

| Service | Port | Schema | Bounded context |
|---|---|---|---|
| `dashboard-bff` | 8081 | — | Composes overview, `/me`, `/health`. No DB, no domain models. |
| `identity-service` | 8082 | `identity` | Tenant, User, UserRelationship |
| `goals-service` | 8083 | `goals` | Goal, GoalContribution |
| `budget-service` | 8084 | `budget` | BudgetCategory |
| `finance-service` | 8085 | `finance` | Account, Transaction, Asset, InvestmentTransaction |
| `portfolio-service` | 8086 | `portfolio` | Holdings (read model) |

Shared jars: `platform-common` (Money, UserId, ErrorEnvelope), `platform-security` (JWT/Cognito, QueryContext).
DDD layers per service: `web → application → domain ← infrastructure`. `domain` depends only on `platform-common` — no cross-service domain imports.

**Data model** (every table carries `tenant_id` + `user_sub`):
```
identity.tenants / users / user_relationships
goals.goals / goal_contributions
finance.accounts        type: BANK | CREDIT_CARD | BROKERAGE | CRYPTO_WALLET
finance.transactions    type: DEBIT | CREDIT | BUY | SELL
  + investment_transactions (1:1 w/ BUY/SELL tx) + assets (STOCK | CRYPTO | ETF)
```

**Multi-tenancy:** `user_sub` = ownership (every WHERE). `tenant_id` = family/org grouping, used only for cross-user queries gated by `user_relationships.can_view_summary`.

**Auth scopes** (`QueryContext`): `finance:own` default (`WHERE user_sub=current`) · `finance:tenant` (`WHERE tenant_id=current`) · `finance:platform` (no filter, admin/analytics only).

**API contracts:** one OpenAPI file per service under `docs/api/` — no controller without a spec entry. `openapi.yaml`(bff), `identity`, `goals`, `budget`, `finance`, `portfolio`.

---

## Conventions

| Item | Convention | Example |
|---|---|---|
| Feature folder / component / SCSS root | kebab-case / `<name>.component.ts` / `.<name>` | `total-budgets/` |
| Page / Repository / Facade | `<name>.page.ts` / `<Name>Repository` (+Mock/Http impl) / `<Name>Facade` | `OverviewRepository` |
| OpenAPI `operationId` | camelCase | `getDashboardOverview` |
| Java aggregate / query-command | PascalCase / `<Action><Subject>Query`\|`Command` | `GetTotalBudgetsQuery` |
| DB table / column | snake_case | `budget_categories`, `user_sub` |
| Branch | `feature/…`, `fix/…`, `docs/…` | |

**Angular:** one BEM root class per component; import only from `shared/ui`; dialogs via `ModalService` + `AppModalComponent` only; mocks in `*-mock.repository.ts` bound via `async` pipe.
**Backend:** spec-first (OpenAPI before any controller); every new table needs `tenant_id` + `user_sub`; scope all queries via `QueryContext`; shared code only in `platform-*` jars; `BigDecimal`/decimal-string, never float; regenerate Postman after any OpenAPI change.

**Doc upkeep:** new widget → this file's widget table. New service → architecture table above + `CLAUDE.md` module table + new `docs/api/<service>.openapi.yaml`. New endpoint → service spec + regenerate Postman. Phase complete → `ROADMAP.md` only.

---

## Design

Dark-mode, data-first dashboard ("Ledgerly"). Shell: 248px sidebar + flex `<main>` (`padding:26px 32px 40px`), 12-col grid (`gap:18px`). Sidebar: logo → account switcher → **MAIN** (Overview, Wallets & Banks, Portfolio, Transactions, Subscriptions, Goals) → **OTHER** (Integrations, Settings, Get Help) → user card (`margin-top:auto`).

**Overview page widgets:**

| Widget | Span | Description |
|---|---|---|
| Net worth | 8 | Hero total, 12M line chart, 1M/3M/12M control |
| Allocation | 4 | Investments/Crypto/Cash bar + legend |
| This month | 4 | Income/spending progress bars, net saved |
| Goals | 4 | 3 goals w/ progress bars, "View all" link |
| Upcoming | 4 | Next 4 recurring payments |
| Recent transactions | 8 | Last 5, icon tile/category/date/amount |
| Top holdings | 4 | 4 holdings, ticker/shares/value/%change |

**Key components:** `DashboardLayoutComponent`, `SidebarComponent`, `PageHeaderComponent` (`layout/`) · `DashboardCardComponent`, `AppModalComponent`, `AppButtonComponent` (`shared/ui/`) · overview widgets under `features/overview/*`.

**Accessibility:** sidebar has `nav` landmark + `aria-current="page"` on the active item; never rely on color alone for positive/negative (always include sign or label); visible focus on every interactive element.

---

## Seed users

| `user_sub` | Notes |
|---|---|
| `seed-user-alice` | Default dev/Postman user |
| `seed-user-bob` | Second user, for isolation tests |

Local dev (pre-Cognito): send header `X-Dev-User-Sub: seed-user-alice` on protected endpoints. Postman: set `{{devUserSub}}` in the environment. Post-Cognito: create real users whose `sub` matches these ids, or update seed SQL to match real `sub` values.
