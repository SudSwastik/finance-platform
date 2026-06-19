-- ── Assets ───────────────────────────────────────────────────────────────────
INSERT INTO finance.assets (id, symbol, name, asset_type) VALUES
    ('aaaaaaaa-0000-0000-0000-000000000001', 'AAPL', 'Apple Inc.', 'STOCK'),
    ('aaaaaaaa-0000-0000-0000-000000000002', 'TSLA', 'Tesla Inc.', 'STOCK'),
    ('aaaaaaaa-0000-0000-0000-000000000003', 'BTC',  'Bitcoin',    'CRYPTO');

-- ── Accounts (Alice: seed-user-alice) ────────────────────────────────────────
INSERT INTO finance.accounts (id, tenant_id, user_sub, type, name, currency) VALUES
    ('bbbbbbbb-0000-0000-0000-000000000001',
     '00000000-0000-0000-0000-000000000001',
     'seed-user-alice', 'BANK',       'HDFC Savings',       'INR'),
    ('bbbbbbbb-0000-0000-0000-000000000002',
     '00000000-0000-0000-0000-000000000001',
     'seed-user-alice', 'CREDIT_CARD','HDFC Regalia Credit', 'INR'),
    ('bbbbbbbb-0000-0000-0000-000000000003',
     '00000000-0000-0000-0000-000000000001',
     'seed-user-alice', 'BROKERAGE',  'Zerodha Demat',       'INR');

-- ── Recurring transactions (Alice) ───────────────────────────────────────────
INSERT INTO finance.transactions
    (id, tenant_id, user_sub, account_id, amount, currency, type, status,
     merchant_name, category, description, is_recurring, transaction_date, created_by)
VALUES
    ('cccccccc-0000-0000-0000-000000000001',
     '00000000-0000-0000-0000-000000000001',
     'seed-user-alice', 'bbbbbbbb-0000-0000-0000-000000000002',
     10.99, 'INR', 'DEBIT', 'SETTLED',
     'Spotify Premium', 'Entertainment', 'Monthly subscription', true, '2025-07-15', 'seed-user-alice'),

    ('cccccccc-0000-0000-0000-000000000002',
     '00000000-0000-0000-0000-000000000001',
     'seed-user-alice', 'bbbbbbbb-0000-0000-0000-000000000002',
     20.00, 'INR', 'DEBIT', 'SETTLED',
     'ChatGPT Plus', 'Software', 'Monthly subscription', true, '2025-07-18', 'seed-user-alice'),

    ('cccccccc-0000-0000-0000-000000000003',
     '00000000-0000-0000-0000-000000000001',
     'seed-user-alice', 'bbbbbbbb-0000-0000-0000-000000000002',
     11.99, 'INR', 'DEBIT', 'SETTLED',
     'YouTube Premium', 'Entertainment', 'Monthly subscription', true, '2025-07-22', 'seed-user-alice');
