# ADR-0007: Multi-module backend (per bounded context)

## Status

Accepted

## Context

ADR-0003 and early backend docs described a **single** Spring Boot application with all domains in one tree. The user wants **separate modules** for Activity Log and other areas — not everything bundled into one deployable.

## Decision

Use a **Maven multi-module monorepo** under `backend/`. Each **bounded context** gets its own **service module** (own Spring Boot app, own DDD layers inside that module). Shared code lives in **library modules only** — no business logic in shared libs.

### Module types

| Type | Naming | Example |
|------|--------|---------|
| Parent | `platform-parent` | `backend/pom.xml` |
| Shared library | `platform-*` | `platform-common`, `platform-security` |
| Service (deployable) | `*-service` | `activity-log-service`, `budget-service` |
| BFF / edge | `*-bff` or `api-gateway` | `dashboard-bff` — aggregates Overview for the UI |

### Default service map (aligned with nav / DESIGN)

| Service module | Bounded context | Own DB schema (dev) | OpenAPI fragment |
|----------------|-----------------|---------------------|------------------|
| `dashboard-bff` | Overview read model (composer) | reads via APIs / projections | `docs/api/dashboard.openapi.yaml` |
| `budget-service` | Budgets, Spending Plan | `budget` | `docs/api/budget.openapi.yaml` |
| `activity-log-service` | Activity Log (ledger / audit trail) | `activity_log` | `docs/api/activity-log.openapi.yaml` |
| `goals-service` | Savings Goals | `goals` | `docs/api/goals.openapi.yaml` |
| `ledger-service` | Transactions (money movement entries) | `ledger` | `docs/api/ledger.openapi.yaml` |
| `portfolio-service` | Investments / Portfolio | `portfolio` | `docs/api/portfolio.openapi.yaml` |
| `recurring-service` | Subscriptions / Recurring | `recurring` | `docs/api/recurring.openapi.yaml` |

**Activity Log** is **not** merged into a monolith — it is implemented only in `activity-log-service`.

### Internal layout (per service)

Each `*-service` module:

```
activity-log-service/
  pom.xml
  src/main/java/com/finance/platform/activitylog/
    domain/
    application/
    infrastructure/
    api/
  src/main/resources/
    application.yml
    db/migration/          # Flyway scoped to this schema
  src/test/
```

Same DDD rules as [BACKEND_ARCHITECTURE.md](../BACKEND_ARCHITECTURE.md); dependencies **do not** cross into another service’s `domain` package.

### Communication between services

| Phase | Approach |
|-------|----------|
| Early (Phase 4c) | `dashboard-bff` may read denormalized data via **internal HTTP** calls to stable APIs, or temporary shared read DB view — prefer HTTP |
| Later | Events (outbox) optional ADR |

No direct import of another service’s JPA entities.

### API surface for the UI

- Angular uses **one public base URL** in dev: `dashboard-bff` (e.g. `localhost:8080`).
- BFF exposes `/api/v1/dashboard/overview` and proxies or composes calls to `budget-service`, `activity-log-service`, etc.
- Direct service URLs exposed in docker-compose for Postman folders per service.

### Docker (dev)

- One Postgres container with **multiple schemas** (one per service), or multiple DBs — document in `docker/README.md`.
- `docker compose` can start **only** the services you need via profiles.

### OpenAPI & Postman

- Split specs per service under `docs/api/`; merged collection for convenience.
- Postman folders: `Dashboard BFF`, `Activity Log`, `Budget`, …

## Alternatives considered

| Option | Rejected because |
|--------|------------------|
| Single Spring Boot with packages only | User explicitly does not want one clubbed deployable |
| Full microservices repo per service | Too heavy for personal project; monorepo modules are enough |
| Shared domain jar with all entities | Creates coupling; violates context boundaries |

## Consequences

- Phase 4 scaffold starts with **parent POM + `platform-common` + `dashboard-bff` + one vertical** (e.g. `budget-service`), then add `activity-log-service` when Activity Log page is built.
- ADR-0003 remains valid for **technology** (Java, Spring, Postgres); module layout is defined here.
- More docker-compose services/ports as modules grow (document in README).

## Supersedes

- Single-tree package layout in early [BACKEND_ARCHITECTURE.md](../BACKEND_ARCHITECTURE.md) — replaced by multi-module layout in that doc.
