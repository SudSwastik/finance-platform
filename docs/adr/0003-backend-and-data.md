# ADR-0003: Backend and data store

## Status

Accepted

## Context

The repo lives under a Java workspace (`finance-platform`). The app needs persistent budgets, transactions, goals, holdings, and recurring bills with correct monetary precision.

## Decision

Use **Java 21** and **Spring Boot 3** (Web, Validation, Data JPA) with **PostgreSQL 16** and **Flyway** migrations.

**Phase 1 data:** seeded PostgreSQL via Docker Compose; no external bank API.

Expose a versioned **REST JSON API** documented with **OpenAPI 3.1**.

## Alternatives considered

| Option | Rejected because |
|--------|------------------|
| Frontend-only + JSON files | No single place for validation and future import pipelines |
| Full-stack TypeScript (Nest) | Duplicates Java preference for this workspace |
| SQLite | Fine for solo use; Postgres chosen for realistic SQL and Testcontainers |
| GraphQL | Overkill for aggregated dashboard reads in MVP |

## Consequences

- `backend/` is a **Maven multi-module** parent; each bounded context is its own `*-service` Spring Boot app — see [ADR-0007](0007-modular-backend-services.md).
- DDD layers live **inside each service module**, not in one shared tree.
- Docker Compose required for local backend development.
- Money modeled as `BigDecimal`; API uses string decimals.
- User-owned rows include `user_sub` aligned with Cognito — see [ADR-0004](0004-auth-cognito.md).
- Spec-first delivery and DDD structure — see [ADR-0006](0006-spec-first-ddd-backend.md).
- Bank aggregation requires a future ADR before implementation.
