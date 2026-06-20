# Ledgerly

Dark-mode personal finance dashboard. Angular SPA + multi-module Spring Boot (DDD) + PostgreSQL + AWS Cognito (Phase 7).

**Status:** Phase 6 in progress — Angular wired to live BFF (totalBudgets, investments, recurring done).

## Docs

| Document | Purpose |
|----------|---------|
| [docs/ROADMAP.md](docs/ROADMAP.md) | Phase checklist |
| [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) | System flow, services, data model |
| [docs/DESIGN.md](docs/DESIGN.md) | UI layout, widgets, component inventory |
| [docs/TECHSTACK.md](docs/TECHSTACK.md) | Stack versions and env vars |
| [docs/CONVENTIONS.md](docs/CONVENTIONS.md) | Naming and rules |
| [docs/adr/](docs/adr/) | Architecture decision records |
| [docs/api/](docs/api/) | OpenAPI specs (one per service) |

## Structure

```
finance-platform/
├── frontend/          # Angular 19 SPA
├── backend/           # Maven multi-module Spring Boot
│   ├── dashboard-bff
│   ├── identity-service
│   ├── budget-service
│   ├── finance-service
│   └── portfolio-service
├── design/            # UI mockups (dc.html)
├── docs/              # Architecture, design, ADRs, OpenAPI
└── infra/local/       # Docker Compose (Postgres)
```

## Run

```bash
# Infrastructure
docker compose -f infra/local/docker-compose.yml up -d

# Backend (from backend/)
./mvnw test
./mvnw -pl dashboard-bff spring-boot:run     # :8080
./mvnw -pl budget-service spring-boot:run    # :8081

# Frontend (from frontend/)
npm install && ng serve
```
