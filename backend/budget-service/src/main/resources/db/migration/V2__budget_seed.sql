-- Seed data for local dev (DESIGN.md sample) — user: seed-user-alice
INSERT INTO budget.user_budget_summary (user_sub, total_display)
VALUES ('seed-user-alice', 6400.00),
       ('seed-user-bob', 3200.00);

INSERT INTO budget.budget_categories (id, user_sub, name, color_token, spent, budget_cap) VALUES
    ('a1000001-0000-4000-8000-000000000001', 'seed-user-alice', 'Essentials', 'category.essentials', 1750.00, 2800.00),
    ('a1000001-0000-4000-8000-000000000002', 'seed-user-alice', 'Lifestyles', 'category.lifestyles', 900.00, 2000.00),
    ('a1000001-0000-4000-8000-000000000003', 'seed-user-alice', 'Occasional', 'category.occasional', 1170.00, 1600.00),
    ('a1000001-0000-4000-8000-000000000004', 'seed-user-alice', 'Others', 'category.others', 1300.00, 2000.00);

INSERT INTO budget.budget_categories (id, user_sub, name, color_token, spent, budget_cap) VALUES
    ('b1000001-0000-4000-8000-000000000001', 'seed-user-bob', 'Essentials', 'category.essentials', 500.00, 1500.00);
