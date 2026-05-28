# Personal Finance Dashboard

Personal finance dashboard from [dashboard.webp](dashboard.webp). **Angular** mockup first · **Multi-module Spring Boot** (separate service per area, e.g. Activity Log) · **Docker** · **Postman** · **Cognito** (later).

**Status:** Documentation complete (stack **accepted**) — ready for Phase 1 scaffold.

## Design

![Dashboard mockup](dashboard.webp) · [docs/DESIGN.md](docs/DESIGN.md)

## Documentation

| Document | Purpose |
|----------|---------|
| [docs/ROADMAP.md](docs/ROADMAP.md) | **Phases 1–8** — mockup → OpenAPI/Docker → backend → wire API → Cognito |
| [docs/FRONTEND_ARCHITECTURE.md](docs/FRONTEND_ARCHITECTURE.md) | Scoped components, shell, ui-kit, modals |
| [docs/BACKEND_ARCHITECTURE.md](docs/BACKEND_ARCHITECTURE.md) | Multi-module services, BFF, DDD per module |
| [docs/adr/0007-modular-backend-services.md](docs/adr/0007-modular-backend-services.md) | Why not one clubbed Spring Boot app |
| [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) | System overview |
| [docs/api/openapi.yaml](docs/api/openapi.yaml) | API contract (source of truth) |
| [docs/api/postman/](docs/api/postman/) | Generated Postman collection |
| [docs/TECH_STACK.md](docs/TECH_STACK.md) | Versions and tools |
| [docs/CONVENTIONS.md](docs/CONVENTIONS.md) | Do's and don'ts |
| [docs/adr/](docs/adr/) | ADRs including ui-kit (0005) and DDD (0006) |
| [AGENTS.md](AGENTS.md) | AI agent briefing |

## Target layout

```
finance-platform/
├── frontend/          # Angular: shell + scoped overview sections + ui-kit
├── backend/           # Maven: dashboard-bff, activity-log-service, budget-service, …
├── docs/api/          # one OpenAPI per service + postman/
├── docker/            # Postgres init + seed
├── docker-compose.yml
└── dashboard.webp
```

## Next step

Start [ROADMAP.md](docs/ROADMAP.md) **Phase 1** (Angular production scaffold).
