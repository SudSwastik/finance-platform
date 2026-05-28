# ADR-0005: UI kit abstraction for swappable component libraries

## Status

Accepted

## Context

The dashboard mockup must be built in Angular with production structure. The user wants to **switch UI libraries** (e.g. Angular Material → PrimeNG) without rewriting feature modules.

## Decision

- Introduce `shared/ui/` — **design system** components with stable selectors (`app-button`, `app-modal`, `app-dashboard-card`).
- Introduce `shared/ui-kit/` — **adapters** that implement rendering with the active library (default: Angular Material).
- Feature and `shell/` code import only from `shared/ui` and facades in `shared/ui-kit` — **never** `MatDialog`, `MatButton` in `features/`.

`AppModalComponent` + `ModalService` is the **only** modal entry point; fixed layout for title, content, footer buttons.

## Consequences

- Slightly more boilerplate when adding new primitive controls.
- Library switch = new `ui-kit/prime` folder + one module swap in `app.config.ts`.
- Modal and button visuals stay consistent across the app by design.
