# CLAUDE.md

Personal finance dashboard. Multi-tenant (tenant → users → accounts → transactions).
**Angular SPA** + **Maven multi-module Spring Boot** (DDD) + **PostgreSQL** + **AWS Cognito** (Phase 7).

Current status: Angular scaffold + auth pages + overview mockup done. Initial backend scaffold done. **Next: Phase 5 — backend architecture refactor.**

See `docs/ROADMAP.md` for phase checklist. See `docs/ARCHITECTURE.md` for full architecture.

---

## Commands

```bash
# Infrastructure
docker compose -f infra/local/docker-compose.yml up -d

# Backend (from backend/)
./mvnw test                                   # all modules
./mvnw -pl budget-service test                # single module
./mvnw -pl budget-service spring-boot:run     # :8081
./mvnw -pl dashboard-bff spring-boot:run      # :8080

# Frontend (from frontend/)
npm install && ng serve
ng test
ng build

# Postman (after any OpenAPI change)
npx openapi-to-postmanv2 \
  -s docs/api/openapi.yaml \
  -o docs/api/postman/finance-platform.postman_collection.json
```

---

## Backend

| Module | Port | Schema | Role |
|--------|------|--------|------|
| `platform-common` | jar | — | `Money`, `UserId`, `ErrorEnvelope` |
| `platform-security` | jar | — | JWT/Cognito autoconfig, `QueryContext`, scope-aware filtering |
| `dashboard-bff` | 8080 | — | Composes overview; `/me`; `/health`; no domain code |
| `identity-service` | 8079 | `identity` | Tenant, User, UserRelationship |
| `budget-service` | 8081 | `budget` | BudgetCategory |
| `activity-log-service` | 8082 | `activity_log` | ActivityLogEntry |
| `goals-service` | 8083 | `goals` | Goal |
| `finance-service` | 8084 | `finance` | Account, Transaction, Asset, InvestmentTransaction |
| `portfolio-service` | 8085 | `portfolio` | Holdings (read model) |

`finance-service` replaces `ledger-service` + `recurring-service`.

**DDD layers** per service (no cross-service domain imports):
```
web → application → domain ← infrastructure
```

**Core data model:**
```
identity.tenants            (id, name, type: PERSONAL|FAMILY|ORG)
identity.users              (id, tenant_id, user_sub, email)
identity.user_relationships (tenant_id, user_sub, related_user_sub, can_view_summary)

finance.accounts            (id, tenant_id, user_sub, type, name, currency)
                            type: BANK | CREDIT_CARD | BROKERAGE | CRYPTO_WALLET
finance.transactions        (id, tenant_id, user_sub, account_id, amount, type, category, description, transaction_date)
finance.investment_transactions (transaction_id, asset_id, quantity, price_per_unit)  -- BUY/SELL only, extends transactions 1:1
finance.assets              (id, symbol, name, asset_type: STOCK|CRYPTO|ETF)
```

**Multi-tenancy:** `user_sub` = ownership boundary (in every WHERE). `tenant_id` = org/family grouping (only for scoped cross-user queries).

**Auth scopes** (enforced by `platform-security`, never by services directly):
- `finance:own` — `WHERE user_sub = current` (default)
- `finance:tenant` — `WHERE tenant_id = current` (family/org dashboard)
- `finance:platform` — no filter (admin/analytics only)

**Spec-first:** edit `docs/api/<service>.openapi.yaml` → implement in that service → update BFF spec if needed → regenerate Postman.

---

## Frontend

Angular 19+, TypeScript strict, standalone components.

| Layer | Rule |
|-------|------|
| `src/styles/` | Global tokens, reset, typography only |
| `app/shell/` | `FixedNavigationComponent`, `TopBarComponent`, `AppShellComponent` — no feature SCSS |
| `app/shared/ui/` | `app-button`, `app-modal`, `app-dashboard-card` — library-agnostic |
| `app/shared/ui-kit/` | Material adapter; features never import `@angular/material/*` directly |
| `app/features/<page>/*/` | One folder per widget; BEM SCSS under one root class |
| `app/data-access/` | Repository interface + mock/HTTP swap via `environment.useMockData` |

Scoped CSS: all rules nested under one root class (`.total-budgets { &__header {} }`).
Modals: `ModalService.open()` + `AppModalComponent` only.
Mock data: `*-mock.repository.ts`; templates bind via `async` pipe through facade — no inline arrays.

---

## Hard rules

- **Money**: `BigDecimal` in Java; decimal strings in JSON. Never `float`/`double`.
- **Auth**: JWT `sub` only. Never trust client-sent userId. Local dev: `X-Dev-User-Sub` header.
- **OpenAPI first**: no controllers without a `docs/api/<service>.openapi.yaml` entry.
- **No cross-context imports**: domain classes must not cross service boundaries.
- **No domain code in BFF**: BFF only composes HTTP responses from services.
- **No UI library in features**: import from `shared/ui` and `shared/ui-kit` only.
- **Every new table**: must have `tenant_id` + `user_sub` columns.
- **Regenerate Postman** on any OpenAPI change.

---

## Naming

| Item | Convention | Example |
|------|------------|---------|
| Section component | kebab folder + `.component.ts` | `total-budgets.component.ts` |
| SCSS root class | matches folder name | `.total-budgets` |
| Repository | interface + mock/http impl | `OverviewRepository`, `OverviewMockRepository` |
| OpenAPI `operationId` | camelCase | `getDashboardOverview` |
| Java aggregate | PascalCase | `BudgetCategory` |
| DB column | snake_case | `user_sub`, `tenant_id`, `account_id` |
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
| Phase checklist | `docs/ROADMAP.md` |
| Full architecture | `docs/ARCHITECTURE.md` |
| Naming + do/don't | `docs/CONVENTIONS.md` |
| Design tokens + widgets | `docs/DESIGN.md` |
| Stack + versions | `docs/TECH_STACK.md` |
| OpenAPI contracts | `docs/api/*.openapi.yaml` |
| Seed test users | `docs/api/seed-users.md` |
| Why multi-module | `docs/adr/0007-modular-backend-services.md` |
| Why Cognito | `docs/adr/0004-auth-cognito.md` |
