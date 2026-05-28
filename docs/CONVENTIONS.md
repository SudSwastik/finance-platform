# Conventions

Standards for Angular mockup, DDD backend, and API testing. Cursor rules in [`.cursor/rules/`](../.cursor/rules/).

---

## Do

### Angular mockup & UI

- Build the full dashboard from **components only** — see [FRONTEND_ARCHITECTURE.md](FRONTEND_ARCHITECTURE.md).
- Put **global** CSS only in `frontend/src/styles/` (tokens, reset, typography).
- Scope feature CSS under one root class per section (e.g. `.total-budgets { }`).
- Use `FixedNavigationComponent` / `TopBarComponent` in `shell/` — not inside overview features.
- Use **`app-modal`** + **`app-button`** for every dialog and primary action.
- Import UI primitives from `shared/ui` and `shared/ui-kit` — not Material in `features/`.
- Mock data in `*.mock.ts` + `*-mock.repository.ts`; templates use facades + `async` pipe.

### Backend

- Add endpoints to the **service’s** OpenAPI (e.g. [activity-log.openapi.yaml](api/activity-log.openapi.yaml)) — not a shared monolith spec only.
- Implement only inside the matching `*-service` module (`activity-log-service`, etc.).
- **Do not** put Activity Log (or other context) code in `dashboard-bff` except HTTP composition.
- **Do not** import another service’s `domain` package.
- Shared code only in `platform-common` / `platform-security` (value objects, JWT — no aggregates).
- Regenerate Postman when any service OpenAPI changes.
- Test: `./mvnw -pl <module> test` + Docker seed for that schema.

### Money, auth, git

- Decimal strings in JSON; `BigDecimal` in Java.
- Scope data by JWT `sub` (or `X-Dev-User-Sub` in local dev only).
- Branch `feature/…`, `fix/…`, `docs/…`; focused PRs per roadmap slice.

---

## Don't

- **Don't** write one-off HTML in root templates to simulate the mockup.
- **Don't** use unscoped global selectors for feature layout (e.g. `.overview h2` in `styles.scss`).
- **Don't** open `MatDialog` / Prime dialog directly in features.
- **Don't** put mock arrays in component templates (`*ngFor="let x of [{...}]"`).
- **Don't** implement controllers before OpenAPI entry exists.
- **Don't** call repositories from controllers.
- **Don't** use float/double for money.
- **Don't** trust client-sent `userId` for scoping.

---

## Naming

| Item | Convention | Example |
|------|------------|---------|
| Section component | `total-budgets.component.ts` | selector `app-total-budgets` |
| SCSS root class | kebab-case matching folder | `.total-budgets` |
| Shell | `fixed-navigation.component.ts` | `app-fixed-navigation` |
| Repository | `OverviewRepository` + `OverviewMockRepository` | |
| OpenAPI `operationId` | camelCase | `getDashboardOverview` |
| Java aggregate | PascalCase | `BudgetCategory` |
| DB / seed | snake_case | `user_sub` |

---

## API & Postman

- Base path `/api/v1/`
- Regenerate: see [api/postman/README.md](api/postman/README.md)
- Postman PR must include collection diff when `openapi.yaml` changes

---

## Testing

| Layer | Expectation |
|-------|-------------|
| Angular section | Component test; assert root scoped class present |
| Angular modal | `ModalService` opens standard shell |
| Domain | Unit tests for Money and invariants |
| Application | Handler tests with fake ports |
| API | `@WebMvcTest` + contract alignment with OpenAPI |
| Integration | Testcontainers + same seed as Docker |

---

## Documentation updates

| Change | Update |
|--------|--------|
| New widget / section | DESIGN.md + scoped component in FRONTEND_ARCHITECTURE |
| New endpoint | openapi.yaml + Postman + BACKEND_ARCHITECTURE slice |
| New UI library | ADR + ui-kit adapter folder |
| Phase scope | ROADMAP.md only |
