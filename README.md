# Ledgerly

Dark-mode personal finance dashboard. Angular SPA + multi-module Spring Boot (DDD) + PostgreSQL + AWS Cognito (Phase 7).

**Status:** Phases 0–6 and 8 done — Angular wired to live BFF/finance-service (overview, transactions, wallets & banks, subscriptions, portfolio pages). Next: Phase 7 — Cognito auth + multi-tenant.

Full architecture, conventions, design system, and phase checklist live in [`CLAUDE.md`](CLAUDE.md) — that's the maintained source of truth for this repo (the old `docs/` tree is local-only now, not version-controlled).

## Services

| Service | Port | Schema | Role |
|---------|------|--------|------|
| `dashboard-bff` | 8081 | — | Composes overview; `/me`; `/health` |
| `identity-service` | 8082 | `identity` | Tenant, User, UserRelationship |
| `budget-service` | 8084 | `budget` | BudgetCategory |
| `finance-service` | 8085 | `finance` | Account, Transaction, Asset, InvestmentTransaction |
| `portfolio-service` | 8086 | `portfolio` | Holdings (read model) |

Shared jars: `platform-common` (Money, UserId, ErrorEnvelope), `platform-security` (JWT/Cognito autoconfig auth).


## Structure

```
finance-platform/
├── frontend/          # Angular 19 SPA
├── backend/           # Maven multi-module Spring Boot
│   ├── platform-common
│   ├── platform-security
│   ├── dashboard-bff
│   ├── identity-service
│   ├── budget-service
│   ├── finance-service
│   └── portfolio-service
├── design/            # UI mockups (dc.html)
└── infra/local/       # Docker Compose (Postgres, LocalStack)
```

## Run

```bash
# Infrastructure
docker compose -f infra/local/docker-compose.yml up -d

# Backend (from backend/)
mvn test
mvn -pl dashboard-bff spring-boot:run      # :8081
mvn -pl finance-service spring-boot:run    # :8085

# Frontend (from frontend/)
npm install && ng serve
```

## Deploy on EC2

See [`infra/prod/README.md`](infra/prod/README.md) for Ubuntu, host Nginx, and HTTPS setup.
Production Docker images build Java and Angular from source; EC2 does not need
Java, Maven, or Node installed. After configuring `infra/prod/.env`, start the stack:

```bash
docker compose --env-file infra/prod/.env -f infra/prod/docker-compose.yml up -d --build
```

Host Nginx proxies the frontend on `127.0.0.1:8090` and API services on
`127.0.0.1:8081–8086`. PostgreSQL is not published to a host port.
