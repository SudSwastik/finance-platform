-- Seed data for local dev (design/Goals.dc.html sample) — user: seed-user-alice
INSERT INTO goals.goals (id, tenant_id, user_sub, name, color_token, current_amount, target_amount, target_date) VALUES
    ('c1000001-0000-4000-8000-000000000001', '00000000-0000-0000-0000-000000000001', 'seed-user-alice', 'Emergency Fund', 'goal.positive', 15600.00, 20000.00, '2026-12-15'),
    ('c1000001-0000-4000-8000-000000000002', '00000000-0000-0000-0000-000000000001', 'seed-user-alice', 'House Down Payment', 'goal.primary', 42000.00, 100000.00, '2028-06-15'),
    ('c1000001-0000-4000-8000-000000000003', '00000000-0000-0000-0000-000000000001', 'seed-user-alice', 'Vacation - Japan', 'goal.neutral', 4500.00, 5000.00, '2026-08-15');

INSERT INTO goals.goal_contributions (id, goal_id, tenant_id, user_sub, amount, note, contributed_at) VALUES
    ('c2000001-0000-4000-8000-000000000001', 'c1000001-0000-4000-8000-000000000001', '00000000-0000-0000-0000-000000000001', 'seed-user-alice', 500.00, 'Monthly auto-deposit', now() - interval '30 days'),
    ('c2000001-0000-4000-8000-000000000002', 'c1000001-0000-4000-8000-000000000002', '00000000-0000-0000-0000-000000000001', 'seed-user-alice', 2000.00, 'Monthly auto-deposit', now() - interval '30 days'),
    ('c2000001-0000-4000-8000-000000000003', 'c1000001-0000-4000-8000-000000000003', '00000000-0000-0000-0000-000000000001', 'seed-user-alice', 250.00, 'Monthly auto-deposit', now() - interval '15 days');
