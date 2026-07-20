# Finance Platform — Backend

Maven multi-module Spring Boot backend (DDD, spec-first, testable).

## Modules

| Module | Port | Description |
|--------|------|-------------|
| `platform-common` | — | `Money`, `UserId`, shared API types |
| `platform-security` | — | Dev `X-Dev-User-Sub` auth (Cognito later) |
| `budget-service` | 8084 | Budget bounded context |
| `dashboard-bff` | 8081 | UI edge: health, me, overview |

## Prerequisites

- Java 21
- Maven 3.9+
- Docker (for Postgres)

## Quick start

```bash
# 1. Database
docker compose up -d

# 2. Run tests
cd backend && mvn test

# 3. Start services (two terminals)
mvn -pl budget-service spring-boot:run
mvn -pl dashboard-bff spring-boot:run
```

## Try APIs

```bash
# Health (public)
curl http://localhost:8081/api/v1/health

# Me
curl -H "X-Dev-User-Sub: seed-user-alice" http://localhost:8081/api/v1/me

# Overview (BFF composes budget-service + stubs)
curl -H "X-Dev-User-Sub: seed-user-alice" http://localhost:8081/api/v1/dashboard/overview

# Budget service directly
curl -H "X-Dev-User-Sub: seed-user-alice" http://localhost:8084/api/v1/budgets/total-budgets
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

- [docs/BACKEND_ARCHITECTURE.md](../docs/BACKEND_ARCHITECTURE.md)
- [docs/api/](../docs/api/)
