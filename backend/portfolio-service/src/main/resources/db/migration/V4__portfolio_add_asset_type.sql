ALTER TABLE portfolio.holdings
    ADD COLUMN IF NOT EXISTS asset_type VARCHAR(8) NOT NULL DEFAULT 'STOCK'
        CHECK (asset_type IN ('STOCK', 'CRYPTO', 'ETF'));

UPDATE portfolio.holdings SET asset_type = 'CRYPTO' WHERE symbol = 'BTC';
