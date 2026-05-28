# ADR-0002: Frontend framework

## Status

Accepted

## Context

The dashboard mockup ([dashboard.webp](../../dashboard.webp)) is a rich client UI with charts, cards, and client-side navigation. The user chose **Angular** over React for the frontend.

## Decision

Use **Angular** (current LTS, target v19+) with the **Angular CLI**, **TypeScript (strict)**, and **standalone components**.

| Concern | Choice |
|---------|--------|
| UI kit | **Angular Material** + SCSS |
| Design tokens | CSS variables from [design-tokens.json](../design-tokens.json) in `styles/_tokens.scss` |
| Charts | **ng2-charts** (Chart.js) for spending line chart |
| Routing | **Angular Router** with lazy-loaded feature routes |
| HTTP / state | `HttpClient` + **RxJS**; optional NgRx only if shared state grows |
| Auth UI | Integrate with Cognito per [ADR-0004](0004-auth-cognito.md) (`aws-amplify` or `angular-oauth2-oidc`) |
| Testing | Jasmine + Karma (default CLI); component tests for Overview |

## Alternatives considered

| Option | Rejected because |
|--------|------------------|
| React + Vite | User preference for Angular |
| Next.js | SSR not required; not Angular ecosystem |
| Vue / Svelte | Not selected |

## Consequences

- `frontend/` is an Angular CLI workspace (`ng serve`, `ng build`).
- **Phase 2** delivers full dashboard mockup via scoped components — see [FRONTEND_ARCHITECTURE.md](../FRONTEND_ARCHITECTURE.md) and [ROADMAP.md](../ROADMAP.md).
- UI library swappable via [ADR-0005](0005-ui-kit-abstraction.md); modals standardized via `AppModalComponent`.
- HTTP + Cognito interceptor in Phase 5–6 (ADR-0004).
- Chart accessibility: provide data table or `aria-label` summary alongside Chart.js canvas.
