# Agent briefing

Personal finance dashboard. **Angular mockup first**. Backend = **Maven multi-module**: one Spring Boot app per bounded context (`activity-log-service`, etc.) + `dashboard-bff`. **Cognito** Phase 6.

## Read first (by task)

| Task | Doc |
|------|-----|
| UI / mockup | [docs/FRONTEND_ARCHITECTURE.md](docs/FRONTEND_ARCHITECTURE.md), [docs/DESIGN.md](docs/DESIGN.md) |
| Current phase | [docs/ROADMAP.md](docs/ROADMAP.md) |
| API / backend slice | [docs/api/README.md](docs/api/README.md), service openapi, [docs/BACKEND_ARCHITECTURE.md](docs/BACKEND_ARCHITECTURE.md), [adr/0007](docs/adr/0007-modular-backend-services.md) |
| System overview | [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) |

## Non-negotiables

1. **No ad-hoc mockup HTML** — only components; mock data in repositories.
2. **Scoped SCSS** — one root class per section (`.total-budgets`, etc.); globals in `src/styles/` only.
3. **Shell** — `FixedNavigationComponent` and `TopBarComponent` are separate under `shell/`.
4. **Modals** — `AppModalComponent` + `ModalService` only; footer uses `app-button`.
5. **UI library** — features use `shared/ui` + `ui-kit` adapters, not Material directly.
6. **Backend** — one module per context; OpenAPI per service; no domain code in BFF; regenerate Postman.
7. **Money** — `BigDecimal` / decimal strings, never float.
8. **Docker** — backend tests use seeded Postgres from `docker/postgres/`.

## Commands (when scaffolded)

```bash
cd frontend && ng serve
cd backend && ./mvnw spring-boot:run
docker compose up -d
npx openapi-to-postmanv2 -s docs/api/openapi.yaml -o docs/api/postman/finance-platform.postman_collection.json
```

## Cursor rules

- `project-core.mdc` — always
- `frontend.mdc` — Angular
- `backend.mdc` — Java / DDD
