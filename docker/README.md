# Docker (local backend testing)

## Purpose

Postgres with **schema + mock seed data** so the backend can be tested without manual SQL. Aligns with [DESIGN.md](../docs/DESIGN.md) sample amounts and [seed-users.md](../docs/api/seed-users.md).

## Layout (created in Phase 3)

```
docker/
├── README.md
└── postgres/
    ├── init/          # create schemas: budget, activity_log, goals, …
    └── seed/          # mock data per schema / user_sub
```

One Postgres instance; **one schema per backend service module** (ADR-0007). Each `*-service` Flyway targets its own schema.

## Commands

```bash
docker compose up -d
docker compose down -v   # reset DB and re-apply seed
```

## Backend connection

```
DATABASE_URL=jdbc:postgresql://localhost:5432/finance_dashboard
```

Flyway migrations in `backend/` should match `init/` baseline or supersede it — document any ordering in BACKEND_ARCHITECTURE.md when scaffold exists.
