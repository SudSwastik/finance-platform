-- Seed data aligned with DESIGN.md — user: seed-user-alice
INSERT INTO portfolio.holdings (id, user_sub, symbol, name, cost_basis, change_percent, current_value) VALUES
    ('31000001-0000-4000-8000-000000000001', 'seed-user-alice', 'AAPL', 'Apple Inc.',          1600.00,  21.90, 1950.00),
    ('31000001-0000-4000-8000-000000000002', 'seed-user-alice', 'BTC',  'Bitcoin',              1100.00, -19.10,  890.00),
    ('31000001-0000-4000-8000-000000000003', 'seed-user-alice', 'TSLA', 'Tesla Inc.',           2000.00,  15.00, 2300.00);

-- user: seed-user-bob
INSERT INTO portfolio.holdings (id, user_sub, symbol, name, cost_basis, change_percent, current_value) VALUES
    ('41000001-0000-4000-8000-000000000001', 'seed-user-bob', 'MSFT', 'Microsoft Corp.', 3000.00, 8.50, 3255.00);
