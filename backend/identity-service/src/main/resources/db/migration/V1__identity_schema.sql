CREATE SCHEMA IF NOT EXISTS identity;

CREATE TABLE identity.tenants (
    id   UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(128) NOT NULL,
    type VARCHAR(16)  NOT NULL CHECK (type IN ('PERSONAL', 'FAMILY', 'ORG'))
);

CREATE TABLE identity.users (
    id        UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID         NOT NULL REFERENCES identity.tenants(id),
    user_sub  VARCHAR(128) NOT NULL UNIQUE,
    email     VARCHAR(255) NOT NULL
);

CREATE INDEX idx_identity_users_user_sub ON identity.users (user_sub);
CREATE INDEX idx_identity_users_tenant_id ON identity.users (tenant_id);

CREATE TABLE identity.user_relationships (
    tenant_id          UUID         NOT NULL REFERENCES identity.tenants(id),
    user_sub           VARCHAR(128) NOT NULL,
    related_user_sub   VARCHAR(128) NOT NULL,
    can_view_summary   BOOLEAN      NOT NULL DEFAULT false,
    PRIMARY KEY (tenant_id, user_sub, related_user_sub)
);
