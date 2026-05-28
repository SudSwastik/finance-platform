# Postman collection

## Files

| File | Purpose |
|------|---------|
| `finance-platform.postman_collection.json` | Generated from [openapi.yaml](../openapi.yaml) |
| `finance-platform.local.postman_environment.json` | `baseUrl`, `bearerToken`, `devUserSub` |

## Regenerate (after openapi.yaml changes)

```bash
npx openapi-to-postmanv2 \
  -s docs/api/openapi.yaml \
  -o docs/api/postman/finance-platform.postman_collection.json
```

Commit the updated collection with the spec PR.

## Usage (Phase 3+)

1. `docker compose up -d`
2. Start backend (Phase 4+): `./mvnw spring-boot:run`
3. Import collection + environment into Postman
4. Run **Health** → **Dashboard / overview** with `X-Dev-User-Sub` until Cognito is wired
