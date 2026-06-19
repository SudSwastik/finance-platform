CREATE SCHEMA IF NOT EXISTS finance;

-- ── Assets (shared reference data) ──────────────────────────────────────────

CREATE TABLE finance.assets (
    id         UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    symbol     VARCHAR(16) NOT NULL UNIQUE,
    name       VARCHAR(128) NOT NULL,
    asset_type VARCHAR(8)  NOT NULL CHECK (asset_type IN ('STOCK', 'CRYPTO', 'ETF'))
);

-- ── Accounts ─────────────────────────────────────────────────────────────────

CREATE TABLE finance.accounts (
    id        UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID         NOT NULL,
    user_sub  VARCHAR(128) NOT NULL,
    type      VARCHAR(16)  NOT NULL CHECK (type IN ('BANK', 'CREDIT_CARD', 'BROKERAGE', 'CRYPTO_WALLET')),
    name      VARCHAR(128) NOT NULL,
    currency  VARCHAR(3)      NOT NULL DEFAULT 'INR'
);

CREATE INDEX idx_finance_accounts_user_sub  ON finance.accounts (user_sub);
CREATE INDEX idx_finance_accounts_tenant_id ON finance.accounts (tenant_id);

-- ── Transactions ─────────────────────────────────────────────────────────────

CREATE TABLE finance.transactions (
    id               UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id        UUID          NOT NULL,
    user_sub         VARCHAR(128)  NOT NULL,
    account_id       UUID          NOT NULL REFERENCES finance.accounts(id),
    amount           NUMERIC(19,4) NOT NULL,
    currency         VARCHAR(3)       NOT NULL DEFAULT 'INR',
    type             VARCHAR(16)   NOT NULL CHECK (type IN ('DEBIT','CREDIT','BUY','SELL','TRANSFER','FEE')),
    status           VARCHAR(16)   NOT NULL DEFAULT 'SETTLED'
                                   CHECK (status IN ('PENDING','SETTLED','FAILED','REVERSED')),
    merchant_name    TEXT,
    merchant_mcc     VARCHAR(4),
    channel          VARCHAR(16)   CHECK (channel IN ('ONLINE','POS','ATM','TRANSFER','UPI','NEFT','RTGS')),
    counterparty     TEXT,
    category         VARCHAR(64),
    description      TEXT,
    reference_id     VARCHAR(64),
    notes            TEXT,
    metadata         JSONB         NOT NULL DEFAULT '{}',
    is_recurring     BOOLEAN       NOT NULL DEFAULT false,
    is_split         BOOLEAN       NOT NULL DEFAULT false,
    transaction_date DATE          NOT NULL,
    entry_source     VARCHAR(32)   NOT NULL DEFAULT 'MANUAL'
                                   CHECK (entry_source IN ('MANUAL','CSV_IMPORT','BANK_API','INGESTION_AGENT')),
    created_by       VARCHAR(128)  NOT NULL,
    updated_by       VARCHAR(128),
    created_at       TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ
);

CREATE INDEX idx_finance_transactions_user_sub        ON finance.transactions (user_sub);
CREATE INDEX idx_finance_transactions_tenant_id       ON finance.transactions (tenant_id);
CREATE INDEX idx_finance_transactions_account_id      ON finance.transactions (account_id);
CREATE INDEX idx_finance_transactions_date            ON finance.transactions (transaction_date DESC);
CREATE INDEX idx_finance_transactions_recurring       ON finance.transactions (user_sub) WHERE is_recurring = true;

-- auto-update updated_at
CREATE OR REPLACE FUNCTION finance.set_updated_at()
RETURNS TRIGGER AS $$
BEGIN NEW.updated_at = now(); RETURN NEW; END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_transactions_updated_at
    BEFORE UPDATE ON finance.transactions
    FOR EACH ROW EXECUTE FUNCTION finance.set_updated_at();

-- ── Investment transactions (BUY/SELL only — extends transactions 1:1) ───────

CREATE TABLE finance.investment_transactions (
    transaction_id UUID          NOT NULL PRIMARY KEY REFERENCES finance.transactions(id) ON DELETE CASCADE,
    asset_id       UUID          NOT NULL REFERENCES finance.assets(id),
    quantity       NUMERIC(36,18) NOT NULL,
    price_per_unit NUMERIC(36,18) NOT NULL
);

-- ── User tags ────────────────────────────────────────────────────────────────

CREATE TABLE finance.user_tags (
    id        UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID         NOT NULL,
    user_sub  VARCHAR(128) NOT NULL,
    name      VARCHAR(64)  NOT NULL,
    color     VARCHAR(7),
    UNIQUE (user_sub, name)
);

CREATE TABLE finance.transaction_tags (
    transaction_id UUID NOT NULL REFERENCES finance.transactions(id) ON DELETE CASCADE,
    tag_id         UUID NOT NULL REFERENCES finance.user_tags(id)    ON DELETE CASCADE,
    PRIMARY KEY (transaction_id, tag_id)
);
