ALTER TABLE portfolio.holdings
    ADD COLUMN IF NOT EXISTS tenant_id UUID;

UPDATE portfolio.holdings SET tenant_id = '00000000-0000-0000-0000-000000000001' WHERE tenant_id IS NULL;

ALTER TABLE portfolio.holdings ALTER COLUMN tenant_id SET NOT NULL;

CREATE INDEX IF NOT EXISTS idx_holdings_tenant_id ON portfolio.holdings (tenant_id);
