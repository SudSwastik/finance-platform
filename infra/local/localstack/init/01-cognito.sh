#!/bin/bash
# Provisions Cognito User Pool + seed users in LocalStack on startup.

set -e

echo "[LocalStack init] Creating Cognito User Pool..."

POOL_ID=$(awslocal cognito-idp create-user-pool \
  --pool-name FinancePlatform \
  --auto-verified-attributes email \
  --username-attributes email \
  --query 'UserPool.Id' \
  --output text)

echo "[LocalStack init] Pool ID: $POOL_ID"

CLIENT_ID=$(awslocal cognito-idp create-user-pool-client \
  --user-pool-id "$POOL_ID" \
  --client-name finance-frontend \
  --no-generate-secret \
  --explicit-auth-flows ALLOW_USER_PASSWORD_AUTH ALLOW_REFRESH_TOKEN_AUTH ALLOW_USER_SRP_AUTH \
  --query 'UserPoolClient.ClientId' \
  --output text)

echo "[LocalStack init] Client ID: $CLIENT_ID"

# Seed user: Alice
awslocal cognito-idp admin-create-user \
  --user-pool-id "$POOL_ID" \
  --username alice@example.com \
  --user-attributes Name=email,Value=alice@example.com Name=email_verified,Value=true \
  --message-action SUPPRESS

awslocal cognito-idp admin-set-user-password \
  --user-pool-id "$POOL_ID" \
  --username alice@example.com \
  --password AlicePass1! \
  --permanent

# Seed user: Bob
awslocal cognito-idp admin-create-user \
  --user-pool-id "$POOL_ID" \
  --username bob@example.com \
  --user-attributes Name=email,Value=bob@example.com Name=email_verified,Value=true \
  --message-action SUPPRESS

awslocal cognito-idp admin-set-user-password \
  --user-pool-id "$POOL_ID" \
  --username bob@example.com \
  --password BobPass1! \
  --permanent

echo "[LocalStack init] Pool ID:   $POOL_ID"
echo "[LocalStack init] Client ID: $CLIENT_ID"
echo "[LocalStack init] Users: alice@example.com / AlicePass1!  |  bob@example.com / BobPass1!"
echo "[LocalStack init] JWKS: http://localhost:4566/${POOL_ID}/.well-known/jwks.json"
echo "[LocalStack init] Done."
