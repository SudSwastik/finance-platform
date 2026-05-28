# API specifications

## Multi-service layout (ADR-0007)

Each backend module has its own OpenAPI file. Do not add Activity Log endpoints to `budget-service` spec.

| File | Service module | Port (local dev) |
|------|----------------|------------------|
| [dashboard.openapi.yaml](dashboard.openapi.yaml) | `dashboard-bff` | 8080 |
| [budget.openapi.yaml](budget.openapi.yaml) | `budget-service` | 8081 |
| [activity-log.openapi.yaml](activity-log.openapi.yaml) | `activity-log-service` | 8082 |
| [goals.openapi.yaml](goals.openapi.yaml) | `goals-service` | 8083 |
| [ledger.openapi.yaml](ledger.openapi.yaml) | `ledger-service` | 8084 |
| [portfolio.openapi.yaml](portfolio.openapi.yaml) | `portfolio-service` | 8085 |
| [recurring.openapi.yaml](recurring.openapi.yaml) | `recurring-service` | 8086 |

## Legacy / merged spec

[openapi.yaml](openapi.yaml) — initial combined contract for Overview + health; split into per-service files as modules are added. BFF may be the only entry in early Phase 4.

## Postman

Regenerate merged collection from all specs (or per-service):

```bash
# Example: merge then convert (tooling TBD in scaffold)
npx openapi-to-postmanv2 -s docs/api/dashboard.openapi.yaml -o docs/api/postman/finance-platform.postman_collection.json
```

Use **folders** per service. Environment variables: `bffUrl`, `activityLogUrl`, `devUserSub`, `bearerToken`.

See [postman/README.md](postman/README.md).
