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

### Services

| Service | Port | Schema | Bounded context |
|---------|------|--------|-----------------|
| `dashboard-bff` | 8080 | — | BFF: composes overview, `/me`, `/health` |
| `identity-service` | 8079 | `identity` | Tenant, User, UserRelationship |
| `budget-service` | 8081 | `budget` | BudgetCategory |
| `finance-service` | 8084 | `finance` | Account, Transaction, Asset |
| `portfolio-service` | 8085 | `portfolio` | Holdings (read model) |

Shared jars: `platform-common` (Money, UserId, ErrorEnvelope), `platform-security` (JWT/Cognito, QueryContext).

### DDD layers (inside every `*-service`)

```
web → application → domain ← infrastructure
```

`domain` depends only on `platform-common`. No cross-service domain imports.

### Data model

```sql
-- identity schema
tenants            (id, name, type)                              -- PERSONAL | FAMILY | ORG
users              (id, tenant_id, user_sub, email)
user_relationships (tenant_id, user_sub, related_user_sub, can_view_summary)

-- finance schema
accounts                (id, tenant_id, user_sub, type, name, currency)
                        -- type: BANK | CREDIT_CARD | BROKERAGE | CRYPTO_WALLET
transactions            (id, tenant_id, user_sub, account_id, amount, type, category, description, transaction_date)
                        -- type: DEBIT | CREDIT | BUY | SELL
investment_transactions (transaction_id, asset_id, quantity, price_per_unit)  -- BUY/SELL only, 1:1
assets                  (id, symbol, name, asset_type)           -- STOCK | CRYPTO | ETF
```

All tables carry `tenant_id` + `user_sub`.

### Multi-tenancy

| Column | Meaning | Used in |
|--------|---------|---------|
| `user_sub` | Ownership | Every WHERE clause (default) |
| `tenant_id` | Grouping (family/org) | Cross-user queries gated by scope |

`user_relationships.can_view_summary = true` unlocks family/org aggregate views.

### Auth scopes (enforced by `platform-security` via `QueryContext`)

| Scope | Query boundary |
|-------|----------------|
| `finance:own` | `WHERE user_sub = current` (default) |
| `finance:tenant` | `WHERE tenant_id = current` |
| `finance:platform` | No filter (admin/analytics only) |

### BFF rules

`dashboard-bff` has no DB, no domain models. Calls domain services via `WebClient`, composes responses for Angular. Exposes `/health`, `/me`, `/dashboard/overview`.

---

## API contracts

One OpenAPI file per service under `docs/api/`. No controller without a spec entry.

| File | Service |
|------|---------|
| `openapi.yaml` | `dashboard-bff` |
| `identity.openapi.yaml` | `identity-service` |
| `budget.openapi.yaml` | `budget-service` |
| `finance.openapi.yaml` | `finance-service` |
| `portfolio.openapi.yaml` | `portfolio-service` |
