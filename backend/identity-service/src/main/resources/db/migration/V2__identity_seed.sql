-- Seed tenant
INSERT INTO identity.tenants (id, name, type)
VALUES ('00000000-0000-0000-0000-000000000001', 'Alice Personal', 'PERSONAL'),
       ('00000000-0000-0000-0000-000000000002', 'Bob Personal',   'PERSONAL');

-- Seed users (user_sub matches X-Dev-User-Sub used in dev)
INSERT INTO identity.users (id, tenant_id, user_sub, email)
VALUES ('00000000-0000-0000-0001-000000000001',
        '00000000-0000-0000-0000-000000000001',
        'seed-user-alice', 'alice@example.com'),
       ('00000000-0000-0000-0001-000000000002',
        '00000000-0000-0000-0000-000000000002',
        'seed-user-bob',   'bob@example.com');
