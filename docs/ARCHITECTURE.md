# Architecture

Angular SPA · Multi-module Spring Boot (DDD) · PostgreSQL · AWS Cognito (Phase 7)

---

## System flow

```
Angular SPA
    │  HTTP + Bearer JWT  (X-Dev-User-Sub in local dev)
    ▼
dashboard-bff :8080
    │  WebClient (parallel)
    ├──► identity-service  :8079
    ├──► budget-service    :8081
    ├──► finance-service   :8084
    └──► portfolio-service :8085
                │
                ▼
          PostgreSQL (1 instance, schema per service)
```

---

## Backend

### Service map

| Service | Port | Schema | Bounded context |
|---------|------|--------|-----------------|
| `dashboard-bff` | 8080 | — | BFF: composes overview, `/me`, `/health` |
| `identity-service` | 8079 | `identity` | Tenant, User, UserRelationship |
| `budget-service` | 8081 | `budget` | BudgetCategory |
| `finance-service` | 8084 | `finance` | Account, Transaction, Asset (replaces `ledger-service` + `recurring-service`) |
| `portfolio-service` | 8085 | `portfolio` | Holdings (read model computed from investment_transactions) |

Shared jars: `platform-common` (Money, UserId, ErrorEnvelope — no aggregates), `platform-security` (JWT/Cognito, QueryContext).

### DDD layers (inside every `*-service`)

```
web (Controller)
  └─► application (QueryHandler / CommandHandler)
         └─► domain (Aggregate + Repository port)
                └─► infrastructure (JPA adapter + Flyway)
```

`domain` depends only on `platform-common`. No cross-service domain imports. `web` calls application handlers only — never repositories directly.

### Data model

**Identity schema:**
```sql
tenants            (id, name, type)                              -- PERSONAL | FAMILY | ORG
users              (id, tenant_id, user_sub, email)
user_relationships (tenant_id, user_sub, related_user_sub, can_view_summary)
```

**Finance schema:**
```sql
accounts               (id, tenant_id, user_sub, type, name, currency)
  -- type: BANK | CREDIT_CARD | BROKERAGE | CRYPTO_WALLET

transactions           (id, tenant_id, user_sub, account_id, amount, type, category, description, transaction_date)
  -- type: DEBIT | CREDIT | BUY | SELL
  -- Bank/card rows stop here.

investment_transactions (transaction_id, asset_id, quantity, price_per_unit)
  -- Only for BUY/SELL rows; extends transactions 1:1. No row for bank/card transactions.

assets                 (id, symbol, name, asset_type)
  -- asset_type: STOCK | CRYPTO | ETF
```

All tables carry `tenant_id` + `user_sub`. Bank/card transactions only use `transactions`. Stock/crypto trades additionally have a row in `investment_transactions`.

### Multi-tenancy

| Column | Meaning | Used in |
|--------|---------|---------|
| `user_sub` | Ownership — individual user | Every WHERE clause (default) |
| `tenant_id` | Grouping — family or org | Cross-user queries gated by scope |

`user_relationships.can_view_summary = true` unlocks family/org aggregate views. Data is never merged — only aggregates are computed across users when explicitly permitted by scope.

### Auth scopes

Enforced by `platform-security` via `QueryContext`. Services never decide which scope applies.

| Scope | Query boundary |
|-------|----------------|
| `finance:own` | `WHERE user_sub = current` (default) |
| `finance:tenant` | `WHERE tenant_id = current` |
| `finance:platform` | No filter (admin/analytics only) |

JWT payload: `{ "sub": "...", "tenant_id": "...", "scope": "finance:own openid" }`

### BFF rules

`dashboard-bff` has no DB, no domain models. It only:
- Calls domain services via `WebClient`
- Composes responses for Angular
- Exposes `/health`, `/me`, `/dashboard/overview`

---

## Frontend

### Layer rules

| Layer | Rule |
|-------|------|
| `src/styles/` | Global tokens, reset, typography only — nothing feature-specific |
| `app/shell/` | `FixedNavigationComponent`, `TopBarComponent`, `AppShellComponent` — separate stable components; no feature SCSS |
| `app/shared/ui/` | `app-button`, `app-modal`, `app-dashboard-card` — library-agnostic primitives |
| `app/shared/ui-kit/` | Swappable adapter (Material today); features never import `@angular/material/*` |
| `app/features/<page>/*/` | One folder per widget; all styles scoped under one BEM root class |
| `app/data-access/` | Repository interface + mock/HTTP swap via `environment.useMockData` |

### Directory layout

```
frontend/src/
├── styles/                    # global tokens, reset, typography only
├── app/
│   ├── shell/
│   │   ├── app-shell/
│   │   ├── fixed-navigation/
│   │   └── top-bar/
│   ├── shared/
│   │   ├── ui/                # app-button, app-modal, dashboard-card
│   │   ├── ui-kit/            # material adapter (swap point)
│   │   └── models/            # TypeScript interfaces
│   ├── features/
│   │   ├── auth/              # login, register, onboarding (outside AppShell)
│   │   └── overview/
│   │       ├── overview.page.ts
│   │       ├── total-budgets/
│   │       ├── investments-widget/
│   │       └── recurring-widget/
│   ├── data-access/
│   │   └── overview/          # overview.repository.ts, overview-mock.repository.ts
│   └── guards/
└── environments/
```

### Scoped CSS

Every section component nests all rules under one root class:
```scss
.total-budgets {
  &__header {}
  &__category-row {}
}
```
`ViewEncapsulation.None` requires an ADR. No bare selectors at file root.

### Modals

`ModalService.open()` + `AppModalComponent` is the only modal path. Never `MatDialog` in features.

### Mock data pattern

```typescript
// facade exposes observable — page templates use async pipe
readonly overview$ = inject(OverviewRepository).getOverview().pipe(shareReplay(1));
```

`OverviewRepository` is injected as mock in dev, HTTP in prod via `providers.ts`.

---

## API contracts

One OpenAPI file per service under `docs/api/`. No controller without a spec entry.

| File | Service | Port |
|------|---------|------|
| `openapi.yaml` | `dashboard-bff` | 8080 |
| `budget.openapi.yaml` | `budget-service` | 8081 |
| `activity-log.openapi.yaml` | `activity-log-service` | 8082 |
| `goals.openapi.yaml` | `goals-service` | 8083 |
| `finance.openapi.yaml` | `finance-service` | 8084 |
| `portfolio.openapi.yaml` | `portfolio-service` | 8085 |

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
