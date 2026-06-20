# ADR-0002: Frontend framework

## Status

Accepted

## Context

Rich client dashboard with charts, cards, and client-side navigation. User chose Angular.

## Decision

**Angular** (19+) with CLI, TypeScript strict, standalone components.

| Concern | Choice |
|---------|--------|
| UI kit | Angular Material + SCSS |
| Design tokens | `docs/design-tokens.json` → `styles/_tokens.scss` |
| Charts | Inline SVG |
| Routing | Angular Router, lazy `loadComponent` / `loadChildren` |
| HTTP / state | `HttpClient` + RxJS; NgRx only if shared state grows |
| Auth UI | Cognito PKCE via `angular-oauth2-oidc` (ADR-0004) |
| Testing | Jasmine + Karma |

## Alternatives considered

| Option | Rejected because |
|--------|------------------|
| React + Vite | User preference for Angular |
| Next.js | SSR not required |
| Vue / Svelte | Not selected |

## Consequences

- `frontend/` is an Angular CLI workspace (`ng serve`, `ng build`).
- Modals via `AppModalComponent` + `ModalService` only.
- Auth interceptor wired in Phase 7 (ADR-0004).
