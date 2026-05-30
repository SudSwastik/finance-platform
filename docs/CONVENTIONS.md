# Conventions

---

## Do

**Angular**
- Build every screen from components — no one-off markup in root templates.
- Scope feature CSS under one root BEM class per section (`.total-budgets {}`); globals only in `src/styles/`.
- Use `ModalService.open()` + `AppModalComponent` for all dialogs.
- Import from `shared/ui` and `shared/ui-kit` — never `@angular/material/*` in features.
- Mock data in `*-mock.repository.ts`; templates bind via `async` pipe through facade.

**Backend**
- Edit `docs/api/<service>.openapi.yaml` before writing any controller.
- Implement only inside the matching `*-service` module.
- Every new DB table must include `tenant_id` + `user_sub`.
- All repository queries scope by `user_sub` via `QueryContext` — never bypass.
- Shared code only in `platform-common` / `platform-security` — no business logic in shared jars.
- Regenerate Postman after any OpenAPI change.

**Money + auth**
- `BigDecimal` in Java; decimal strings in JSON. Never `float`/`double`.
- Scope data by JWT `sub` via `QueryContext`. Never trust client-sent `userId`.
- Local dev: `X-Dev-User-Sub` header only. Strip in prod.

---

## Don't

- Don't write inline mock arrays in component templates.
- Don't use unscoped global selectors for feature layout.
- Don't call repositories from controllers.
- Don't import another service's domain package.
- Don't put domain logic in `dashboard-bff`.
- Don't use `float`/`double` for money anywhere in the stack.
- Don't add a column to a new table without `tenant_id` + `user_sub`.

---

## Naming

| Item | Convention | Example |
|------|------------|---------|
| Feature component folder | kebab-case | `total-budgets/` |
| Component file | `<folder>.component.ts` | `total-budgets.component.ts` |
| SCSS root class | matches folder name | `.total-budgets` |
| Page component | `<name>.page.ts` | `overview.page.ts` |
| Repository | `<Name>Repository` + `<Name>MockRepository` / `<Name>HttpRepository` | `OverviewRepository` |
| Facade | `<Name>Facade` | `OverviewFacade` |
| OpenAPI `operationId` | camelCase | `getDashboardOverview` |
| Java aggregate | PascalCase | `BudgetCategory` |
| Java query/command | `<Action><Subject>Query/Command` | `GetTotalBudgetsQuery` |
| DB table | snake_case | `budget_categories` |
| DB column | snake_case | `user_sub`, `tenant_id`, `account_id` |
| Branch | `feature/…`, `fix/…`, `docs/…` | `feature/finance-service` |

---

## Testing

| Layer | Expectation |
|-------|-------------|
| Angular section | Assert scoped root class present; no Material internals |
| Angular modal | `ModalService` opens `AppModalComponent` with correct config |
| Java domain/application | Unit tests; no Spring context; fake ports |
| Java API slice | `@WebMvcTest`; contract matches OpenAPI |
| Java BFF | `MockWebServer` for each downstream service |
| Integration | Testcontainers + same Flyway seed as Docker |

---

## When to update docs

| Change | Update |
|--------|--------|
| New widget | `DESIGN.md` component inventory + `ARCHITECTURE.md` directory layout |
| New service | `ARCHITECTURE.md` service map + `CLAUDE.md` module table + new `docs/api/<service>.openapi.yaml` + `adr/0007` |
| New OpenAPI endpoint | Service spec + regenerate Postman |
| New UI library | ADR + `shared/ui-kit/<lib>/` adapter |
| Phase complete | `ROADMAP.md` only |
