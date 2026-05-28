# ADR-0004: Authentication and authorization with AWS Cognito

## Status

Accepted

## Context

The dashboard is a personal finance app that must support secure sign-in, API protection, and per-user data isolation. Credentials must not be stored in the application database. The user chose **AWS Cognito** for authentication and authorization.

## Decision

### Authentication (who you are)

- **Amazon Cognito User Pool** for user directory, sign-up/sign-in, password policy, and MFA (optional).
- **SPA app client** with **PKCE** (no client secret in the browser).
- Angular obtains **JWTs** (ID token + access token) after login via:
  - **Preferred:** Cognito Hosted UI + OIDC (`angular-oauth2-oidc`), or
  - **Alternative:** AWS Amplify Auth (`@aws-amplify/ui-angular`).
- Angular `authInterceptor` sends `Authorization: Bearer <access_token>` on every API request.

### Authorization (what you can do)

| Layer | Mechanism |
|-------|-----------|
| **API gateway (Spring)** | Spring Security **OAuth2 Resource Server** validates JWT signature and issuer against Cognito JWKS |
| **User scoping** | All domain data keyed by Cognito `sub` claim; services always filter by authenticated `sub` |
| **Roles / groups** | Cognito **Groups** (e.g. `admin`, `user`) mapped to `cognito:groups` in JWT; use `@PreAuthorize("hasRole('USER')")` or custom `PermissionEvaluator` |
| **Fine-grained (later)** | App-specific permissions in DB only if groups are insufficient — still keyed by `sub` |

### Backend configuration

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://cognito-idp.${COGNITO_REGION}.amazonaws.com/${COGNITO_USER_POOL_ID}
```

Extract principal: `JwtAuthenticationToken` → `sub` (user id), `cognito:groups` (roles).

### Public vs protected routes

| Path | Auth |
|------|------|
| `/api/v1/health` | Public |
| `/api/v1/dashboard/**` | Authenticated |
| `/actuator/health` | Public (dev only) |

### Local development

- Use a dedicated **dev User Pool** (or shared team pool) — never production pool credentials in repo.
- Document pool id, client id, region in `.env.example` only.
- Optional: LocalStack Cognito for offline dev (advanced; not required for MVP).

## Alternatives considered

| Option | Rejected because |
|--------|------------------|
| Spring Security form login + DB users | Duplicates Cognito; weaker for SPA |
| Auth0 / Firebase | User chose AWS Cognito |
| API keys only | No real user identity or multi-device sessions |

## Consequences

- **Phase 1** includes Cognito wiring before Overview ships (see [ROADMAP.md](../ROADMAP.md)).
- Backend never implements password storage.
- Frontend routes use `authGuard`; unauthenticated users redirect to login.
- E2E tests need a test Cognito user or mocked JWT in dev profile only.
- New environments (staging/prod) each get their own User Pool or pool app client.

## Security notes

- Do not commit User Pool IDs tied to secrets; client id for SPA is public OK.
- Enable HTTPS in production for Angular and API.
- Token refresh handled by OIDC/Amplify library — do not store tokens in `localStorage` if avoidable; prefer sessionStorage or library defaults.
