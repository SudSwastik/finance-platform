# ADR-0001: Record architecture decisions

## Status

Accepted

## Context

This project spans UI, API, data, and future integrations. Ad-hoc technology choices will be hard to reverse without a written record.

## Decision

We use Architecture Decision Records (ADRs) in `docs/adr/`:

- One file per decision, numbered sequentially (`0002-…`, `0003-…`).
- Each ADR has: **Status**, **Context**, **Decision**, **Consequences**.
- Status values: Proposed | Accepted | Deprecated | Superseded
- [docs/TECH_STACK.md](../TECH_STACK.md) remains the version inventory; ADRs explain **why**.

## Consequences

- New significant choices (framework, database, auth provider) get an ADR before implementation.
- Superseded ADRs stay in place with a note pointing to the replacement.
