# Conventions

---

## Naming

| Item | Convention | Example |
|------|------------|---------|
| Feature component folder | kebab-case | `total-budgets/` |
| Component file | `<folder>.component.ts` | `total-budgets.component.ts` |
| SCSS root class | matches folder | `.total-budgets` |
| Page | `<name>.page.ts` | `overview.page.ts` |
| Repository | `<Name>Repository` + `Mock`/`Http` impl | `OverviewRepository` |
| Facade | `<Name>Facade` | `OverviewFacade` |
| OpenAPI `operationId` | camelCase | `getDashboardOverview` |
| Java aggregate | PascalCase | `BudgetCategory` |
| Java query/command | `<Action><Subject>Query/Command` | `GetTotalBudgetsQuery` |
| DB table | snake_case | `budget_categories` |
| DB column | snake_case | `user_sub`, `tenant_id` |
| Branch | `feature/…`, `fix/…`, `docs/…` | `feature/finance-service` |

---

## Rules

**Angular** — scope CSS under one BEM root class; import only from `shared/ui`; use `ModalService` + `AppModalComponent` for dialogs; mock data in `*-mock.repository.ts` bound via `async` pipe.

**Backend** — spec first (`docs/api/<service>.openapi.yaml` before any controller); every new table needs `tenant_id` + `user_sub`; scope all queries via `QueryContext`; shared code only in `platform-*` jars; `BigDecimal` in Java / decimal string in JSON; regenerate Postman after any OpenAPI change.

---

## When to update docs

| Change | Update |
|--------|--------|
| New widget | `DESIGN.md` component inventory |
| New service | `ARCHITECTURE.md` service map + `CLAUDE.md` module table + new `docs/api/<service>.openapi.yaml` |
| New OpenAPI endpoint | Service spec + regenerate Postman |
| Phase complete | `ROADMAP.md` only |
