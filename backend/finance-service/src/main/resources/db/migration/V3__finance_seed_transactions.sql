-- Additional account: crypto wallet
INSERT INTO finance.accounts (id, tenant_id, user_sub, type, name, currency) VALUES
    ('bbbbbbbb-0000-0000-0000-000000000004',
     '00000000-0000-0000-0000-000000000001',
     'seed-user-alice', 'CRYPTO_WALLET', 'CoinDCX Wallet', 'INR');

-- Additional assets
INSERT INTO finance.assets (id, symbol, name, asset_type) VALUES
    ('aaaaaaaa-0000-0000-0000-000000000004', 'NVDA', 'NVIDIA Corp.', 'STOCK'),
    ('aaaaaaaa-0000-0000-0000-000000000005', 'ETH',  'Ethereum',     'CRYPTO');

-- ── Regular transactions — June 2026 ─────────────────────────────────────────
-- (used by "This month" + "Recent transactions" widgets)

INSERT INTO finance.transactions
    (id, tenant_id, user_sub, account_id, amount, currency, type, status,
     merchant_name, category, description, is_recurring, transaction_date, created_by)
VALUES
    -- Income
    ('dddddddd-0000-0000-0000-000000000001',
     '00000000-0000-0000-0000-000000000001',
     'seed-user-alice', 'bbbbbbbb-0000-0000-0000-000000000001',
     85000.00, 'INR', 'CREDIT', 'SETTLED',
     'Acme Corp', 'Income', 'Monthly salary', false, '2026-06-18', 'seed-user-alice'),

    -- Expenses
    ('dddddddd-0000-0000-0000-000000000002',
     '00000000-0000-0000-0000-000000000001',
     'seed-user-alice', 'bbbbbbbb-0000-0000-0000-000000000002',
     2840.00, 'INR', 'DEBIT', 'SETTLED',
     'Zepto', 'Groceries', 'Weekly groceries', false, '2026-06-17', 'seed-user-alice'),

    ('dddddddd-0000-0000-0000-000000000003',
     '00000000-0000-0000-0000-000000000001',
     'seed-user-alice', 'bbbbbbbb-0000-0000-0000-000000000002',
     42000.00, 'INR', 'DEBIT', 'SETTLED',
     'Croma', 'Electronics', 'Laptop accessory', false, '2026-06-15', 'seed-user-alice'),

    ('dddddddd-0000-0000-0000-000000000004',
     '00000000-0000-0000-0000-000000000001',
     'seed-user-alice', 'bbbbbbbb-0000-0000-0000-000000000001',
     3200.00, 'INR', 'DEBIT', 'SETTLED',
     'HP Petrol', 'Fuel', 'Fuel refill', false, '2026-06-14', 'seed-user-alice'),

    ('dddddddd-0000-0000-0000-000000000005',
     '00000000-0000-0000-0000-000000000001',
     'seed-user-alice', 'bbbbbbbb-0000-0000-0000-000000000002',
     8500.00, 'INR', 'DEBIT', 'SETTLED',
     'Swiggy', 'Food & Dining', 'Food orders', false, '2026-06-12', 'seed-user-alice'),

    ('dddddddd-0000-0000-0000-000000000006',
     '00000000-0000-0000-0000-000000000001',
     'seed-user-alice', 'bbbbbbbb-0000-0000-0000-000000000001',
     18500.00, 'INR', 'DEBIT', 'SETTLED',
     'Landlord', 'Rent', 'Monthly rent', false, '2026-06-01', 'seed-user-alice'),

    -- BUY investment transactions
    ('dddddddd-0000-0000-0000-000000000007',
     '00000000-0000-0000-0000-000000000001',
     'seed-user-alice', 'bbbbbbbb-0000-0000-0000-000000000004',
     42000.00, 'INR', 'BUY', 'SETTLED',
     'CoinDCX', 'Investment', 'BTC purchase', false, '2026-06-16', 'seed-user-alice'),

    ('dddddddd-0000-0000-0000-000000000008',
     '00000000-0000-0000-0000-000000000001',
     'seed-user-alice', 'bbbbbbbb-0000-0000-0000-000000000003',
     13600.00, 'INR', 'BUY', 'SETTLED',
     'Zerodha', 'Investment', 'AAPL purchase', false, '2026-06-10', 'seed-user-alice');

INSERT INTO finance.investment_transactions (transaction_id, asset_id, quantity, price_per_unit) VALUES
    ('dddddddd-0000-0000-0000-000000000007',
     'aaaaaaaa-0000-0000-0000-000000000003',
     0.05, 840000.00),   -- 0.05 BTC @ ₹8,40,000

    ('dddddddd-0000-0000-0000-000000000008',
     'aaaaaaaa-0000-0000-0000-000000000001',
     8.0, 1700.00);      -- 8 AAPL @ ₹1,700
