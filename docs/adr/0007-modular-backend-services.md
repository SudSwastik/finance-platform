# ADR-0007: Multi-module backend (per bounded context)

## Status

Accepted

## Context

Each bounded context needs its own Spring Boot module with DDD layers, schema, and OpenAPI spec. Shared code lives in library modules only.

## Decision

Maven multi-module monorepo under `backend/`. One Spring Boot deployable per bounded context.

### Module types

| Type | Naming | Example |
|------|--------|---------|
| Parent | `platform-parent` | `backend/pom.xml` |
| Shared library | `platform-*` | `platform-common`, `platform-security` |
| Service | `*-service` | `finance-service`, `budget-service` |
| BFF | `*-bff` | `dashboard-bff` |

### Service map

| Service | Port | Schema | Bounded context |
|---------|------|--------|-----------------|
| `dashboard-bff` | 8080 | — | BFF composer |
| `identity-service` | 8079 | `identity` | Tenant, User, UserRelationship |
| `budget-service` | 8081 | `budget` | BudgetCategory |
| `finance-service` | 8084 | `finance` | Account, Transaction, Asset |
| `portfolio-service` | 8085 | `portfolio` | Holdings (read model) |

### Internal layout (per service)

```
finance-service/
  src/main/java/com/finance/platform/finance/
    domain/
    application/
    infrastructure/
    web/
  src/main/resources/db/migration/   # Flyway scoped to finance schema
```

### Communication

`dashboard-bff` calls domain services via `WebClient` (HTTP). No direct JPA entity imports across services. One OpenAPI spec per service under `docs/api/`.

## Alternatives considered

| Option | Rejected because |
|--------|------------------|
| Single Spring Boot (packages only) | No per-context deployability or schema isolation |
| Full microservices (separate repos) | Overhead not justified for personal project |
| Shared domain jar | Creates coupling across context boundaries |

## Consequences

- More docker-compose services as modules grow.
- ADR-0003 remains valid for technology choices; module layout defined here.
