# ADR-0006: Spec-first API and DDD backend

## Status

Accepted

## Context

The user requires backend work to start from **specifications**, grow **incrementally**, use **Docker mock/seed data** for testing, export a **Postman collection**, and follow **DDD** so integrations can be swapped later.

## Decision

1. **OpenAPI** in `docs/api/openapi.yaml` is the contract source; controllers implement the spec.
2. Backend packages follow **DDD**: `domain` → `application` → `infrastructure` / `api`.
3. **Docker Compose** runs Postgres with init + seed SQL matching DESIGN sample data.
4. **Postman collection** generated from OpenAPI and committed under `docs/api/postman/`.
5. Each feature slice: spec PR → domain/port → handler → adapter → controller → tests → regenerate Postman.

## Alternatives considered

| Option | Rejected because |
|--------|------------------|
| Code-first OpenAPI only | Spec not reviewable before implementation |
| Transaction script in controllers | No swap-friendly boundaries |
| H2 in-memory only | Diverges from Docker/Postgres production path |

## Consequences

- No endpoint without openapi.yaml entry (per service — see [ADR-0007](0007-modular-backend-services.md)).
- DDD layers are repeated **inside each** `*-service` module, not once in a monolith.
- Flyway migrations align with seed scripts per schema.
- CI can validate OpenAPI + run integration tests against Testcontainers.
