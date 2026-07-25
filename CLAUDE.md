# CLAUDE.md

Personal finance dashboard. Multi-tenant (tenant → users → accounts → transactions).
**Angular SPA** + **Maven multi-module Spring Boot** (DDD) + **PostgreSQL** + **AWS Cognito** (Phase 7).

Current status: Backend architecture refactor done (Phase 5). Angular wired to live BFF/finance-service — overview + transactions pages (Phase 6). Remaining nav pages (Wallets & Banks, Subscriptions, Portfolio) done (Phase 8). **Next: Phase 7 — Cognito auth + multi-tenant.**

See `docs/ROADMAP.md` for phase checklist. See `docs/ARCHITECTURE.md` for full architecture.

---

## Commands

```bash
# Infrastructure
docker compose -f infra/local/docker-compose.yml up -d

# Backend (from backend/)
./mvnw test                                   # all modules
./mvnw -pl budget-service test                # single module
./mvnw -pl budget-service spring-boot:run     # :8084
./mvnw -pl dashboard-bff spring-boot:run      # :8081

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
| `dashboard-bff` | 8081 | — | Composes overview; `/me`; `/health`; no domain code |
| `identity-service` | 8082 | `identity` | Tenant, User, UserRelationship |
| `goals-service` | 8083 | `goals` | Goal, GoalContribution |
| `budget-service` | 8084 | `budget` | BudgetCategory |
| `finance-service` | 8085 | `finance` | Account, Transaction, Asset, InvestmentTransaction |
| `portfolio-service` | 8086 | `portfolio` | Holdings (read model) |

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

goals.goals                 (id, tenant_id, user_sub, name, color_token, current_amount, target_amount, target_date)
goals.goal_contributions    (id, goal_id, tenant_id, user_sub, amount, note, contributed_at)

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
| `app/layout/` | `DashboardLayoutComponent`, `SidebarComponent`, `PageHeaderComponent` — no feature SCSS |
| `app/shared/ui/` | `app-button`, `app-modal`, `app-dashboard-card` — library-agnostic |
| `app/features/<page>/*/` | One folder per widget; BEM SCSS under one root class |
| `app/data-access/` | Repository interface + mock/HTTP swap via `environment.useMockData` |

Scoped CSS: all rules nested under one root class (`.total-budgets { &__header {} }`).
Modals: `ModalService.open()` + `AppModalComponent` only.
Mock data: `*-mock.repository.ts`; templates bind via `async` pipe through facade — no inline arrays.

**Product:** Ledgerly, a dark-mode personal finance dashboard. Dense, calm, data-first. No gradients, no emoji, no rounded "accent-border" cards.

---

## Fonts
- **Family:** `Manrope` (Google Fonts), weights 400/500/600/700/800. Fallback `system-ui, -apple-system, sans-serif`.
- Numbers always use `font-variant-numeric: tabular-nums` (set once on the page wrapper).
- Headings are tight: page title `font-size:24px; font-weight:800; letter-spacing:-.02em`. Big stat numbers `38px/800` (hero) or `24px/800` (stat cards).

```
<link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&display=swap" rel="stylesheet">
font-family:'Manrope',system-ui,-apple-system,sans-serif;
```

## Icons
- **Phosphor Icons**, weight `regular`. `<script src="https://unpkg.com/@phosphor-icons/web@2.1.1"></script>`
- Usage: `<i class="ph ph-wallet"></i>`. Reset: `i.ph{display:inline-flex;align-items:center;justify-content:center;line-height:1;}`
- Nav/UI icons ~17px, inline icons 13–15px.

---

## Colors

### Surfaces (darkest → lightest)
| Token | Hex | Use |
|---|---|---|
| App background | `#08080A` | page behind everything |
| Sidebar / inset wells | `#0C0C0F` | sidebar, segmented-control track |
| Card | `#121215` | every content card / panel |
| Control / search / button-secondary | `#141417` | input pills, icon buttons |
| Hover fill | `#1A1A1E` | secondary button/row hover |
| Active nav item | `#1A1A1F` | selected sidebar link bg |
| Icon tile (neutral) | `#1C1C21` | small icon squares in lists |
| Track / progress base | `#1E1E23` | progress bar background |
| Avatar / chip tile | `#23232A` | round avatar, segmented active pill |

### Borders
| Token | Hex | Use |
|---|---|---|
| Hairline (sidebar/divider) | `#1A1A1F` | sidebar border, footer divider |
| Row divider (subtle) | `#1E1E23` | list row separators |
| Row divider (faint) | `#16161A` | table row separators + hover bg |
| Card border | `#222227` | every card outline |
| Control border | `#242429` | inputs, secondary buttons |

### Text
| Token | Hex | Use |
|---|---|---|
| Primary heading | `#F2F2F4` | titles, big numbers |
| Body strong | `#ECECEE` | row labels, values, active nav |
| Body soft | `#C8C8CD` / `#A8A8AE` | secondary values |
| Muted | `#84848B` | subtitles, inactive nav, labels |
| Faint | `#6C6C73` | meta, dates, sublabels |
| Faintest | `#55555C` / `#5C5C63` | section captions, axis labels |

### Accent & semantic
| Token | Hex | Use |
|---|---|---|
| **Primary (brand blue)** | `#6E8FD6` | logo, primary button, charts, progress, active |
| Primary hover | `#82A0E0` | primary button hover |
| Primary icon tint | `#8FAAE0` | active nav icon |
| Positive (green) | `#4FAE85` | gains, income, "+" amounts |
| Positive bg | `#16271F` | green pill/icon-tile background |
| Negative (red/clay) | `#D2796F` | losses, spending, alerts |
| Warning (gold) | `#C9A24B` | BUY tags |
| Warning bg | `#29230F` | gold pill background |
| Neutral chart 2 | `#6E6E76` | secondary chart segment (crypto) |
| Neutral chart 3 | `#39393F` | tertiary chart segment (cash) |

> On the primary blue button, text/icon is `#0C0C0F` (near-black), not white.

---

## Layout
- **Shell:** flex row, `min-height:100vh`. Fixed sidebar + flexible `<main>`.
- **Sidebar:** `width:248px; flex:none`, bg `#0C0C0F`, right border `#1A1A1F`, `padding:20px 16px`, sticky full-height. Structure: logo → account switcher → `MAIN` group → `OTHER` group → user card pinned with `margin-top:auto`.
  - Section captions: `font-size:10px; font-weight:700; letter-spacing:.12em; color:#55555C` (uppercase).
  - Nav link: `padding:9px 12px; border-radius:9px; font-size:13.5px; gap:12px`. Active = bg `#1A1A1F` + weight 600 + tinted icon; inactive = `#84848B`, hover `background:#141417;color:#ECECEE`.
- **Main:** `padding:26px 32px 40px; gap:20px`. Header row = title block + actions, then a `repeat(12,1fr)` grid with `gap:18px`. Stat strips use `repeat(4,1fr)`.

## Components
- **Card:** `background:#121215; border:1px solid #222227; border-radius:16px; padding:22px 24px`. Stat cards use `border-radius:14px; padding:18px 20px`.
- **Card title:** `font-size:14px; font-weight:700; color:#F2F2F4`, optional muted subtitle below.
- **Primary button:** `height:38px; background:#6E8FD6; color:#0C0C0F; font-weight:700; border-radius:10px; padding:0 16px; gap:8px`; hover `#82A0E0`.
- **Icon button / input pill:** `background:#141417; border:1px solid #242429; border-radius:10px`.
- **Segmented control:** track `background:#0C0C0F; border:1px solid #222227; border-radius:9px; padding:3px`; active segment `background:#23232A; color:#ECECEE; font-weight:700`; inactive `color:#84848B`. Item `padding:5px 11px; border-radius:7px; font-size:12px`.
- **Pill / tag:** `padding:3px 8px–4px 9px; border-radius:7px; font-size:11–12.5px; font-weight:700`. Pair semantic text color with its `*-bg` (green `#4FAE85`/`#16271F`, gold `#C9A24B`/`#29230F`).
- **Progress bar:** track `height:7px; background:#1E1E23; border-radius:4px`; fill in primary or semantic color.
- **List row:** `padding:11px 0`, separated by `border-top:1px solid #1E1E23`. Leading 32–38px rounded icon tile, flex-1 label/sublabel, right-aligned value. Tables divide with `#16161A` and hover to `#16161A`.
- **Charts (inline SVG):** line `#6E8FD6` width 2.5, area fill = vertical gradient of `#6E8FD6` 0.28→0; gridlines `#1C1C21`; endpoint dot `#6E8FD6` with card-colored stroke. Axis labels `#5C5C63; font-size:11px`. Do not hand-draw complex SVG art — only these data shapes.

## Radii
`16px` cards · `14px` stat cards · `10px` buttons/inputs/logo · `9px` nav items / segmented track / icon tiles · `7px` pills / segment items · `4px` progress bars · `50%` avatars & dots.

## Rules
- One accent only (`#6E8FD6`). Green/red/gold are semantic, never decorative.
- Everything sits on a card; cards sit on `#08080A`. Keep borders subtle.
- Money: tabular nums, `+`/`−` (real minus `&minus;`), green for positive, red for negative, primary text for neutral.
- Match existing spacing scale (gaps 18–20px between blocks, 9–14px inside).

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
| Design reference + widgets | `docs/DESIGN.md` |
| Stack + versions | `docs/TECHSTACK.md` |
| OpenAPI contracts | `docs/api/*.openapi.yaml` |
| Seed test users | `docs/SEEDUSERS.md` |
| Why multi-module | `docs/adr/0007-modular-backend-services.md` |
| Why Cognito | `docs/adr/0004-auth-cognito.md` |
