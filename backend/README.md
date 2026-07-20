# Finance Platform — Backend

Maven multi-module Spring Boot backend (DDD, spec-first, testable).

## Modules

| Module | Port | Description |
|--------|------|-------------|
| `platform-common` | — | `Money`, `UserId`, shared API types |
| `platform-security` | — | Dev `X-Dev-User-Sub` auth (Cognito later) |
| `dashboard-bff` | 8081 | UI edge: health, me, overview |
| `identity-service` | 8082 | Tenant, User, UserRelationship |
| `budget-service` | 8084 | BudgetCategory |
| `finance-service` | 8085 | Account, Transaction, Asset, InvestmentTransaction |
| `portfolio-service` | 8086 | Holdings (read model) |

## Prerequisites

- Java 21
- Maven 3.9+
- Docker (for Postgres)

## Quick start

```bash
# 1. Database + infra
docker compose -f ../infra/local/docker-compose.yml up -d

# 2. Run tests
mvn test

# 3. Start services (separate terminals as needed)
mvn -pl dashboard-bff spring-boot:run
mvn -pl finance-service spring-boot:run
```

## Try APIs

```bash
# Health (public)
curl http://localhost:8081/api/v1/health

# Me
curl -H "X-Dev-User-Sub: seed-user-alice" http://localhost:8081/api/v1/me

# Overview (BFF composes budget/finance/portfolio services)
curl -H "X-Dev-User-Sub: seed-user-alice" http://localhost:8081/api/v1/dashboard/overview

# finance-service directly
curl -H "X-Dev-User-Sub: seed-user-alice" http://localhost:8085/api/v1/finance/accounts
```

## Tests

| Layer | Example |
|-------|---------|
| Domain | `platform-common` → `MoneyTest` |
| Application | `GetTotalBudgetsQueryHandlerTest` |
| API | `BudgetControllerWebMvcTest` |
| Integration | `BudgetCategoryRepositoryAdapterIntegrationTest` (Testcontainers) |
| BFF | `OverviewComposerTest` |

## Docs

Full architecture, DDD layering, multi-tenancy rules, and hard rules live in [`../CLAUDE.md`](../CLAUDE.md) — the maintained source of truth for this repo.
