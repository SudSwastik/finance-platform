ALTER TABLE budget.budget_categories
    ADD COLUMN IF NOT EXISTS tenant_id UUID;

ALTER TABLE budget.user_budget_summary
    ADD COLUMN IF NOT EXISTS tenant_id UUID;

-- Backfill existing rows with a placeholder UUID (Phase 7 will wire to real tenant_id from Cognito)
UPDATE budget.budget_categories SET tenant_id = '00000000-0000-0000-0000-000000000001' WHERE tenant_id IS NULL;
UPDATE budget.user_budget_summary SET tenant_id = '00000000-0000-0000-0000-000000000001' WHERE tenant_id IS NULL;

ALTER TABLE budget.budget_categories ALTER COLUMN tenant_id SET NOT NULL;
ALTER TABLE budget.user_budget_summary ALTER COLUMN tenant_id SET NOT NULL;

CREATE INDEX IF NOT EXISTS idx_budget_categories_tenant_id ON budget.budget_categories (tenant_id);
