CREATE SCHEMA IF NOT EXISTS portfolio;

CREATE TABLE portfolio.holdings (
    id             UUID PRIMARY KEY,
    user_sub       VARCHAR(128)   NOT NULL,
    symbol         VARCHAR(16)    NOT NULL,
    name           VARCHAR(128)   NOT NULL,
    cost_basis     NUMERIC(19, 2) NOT NULL,
    change_percent NUMERIC(6, 2)  NOT NULL,
    current_value  NUMERIC(19, 2) NOT NULL
);

CREATE INDEX idx_holdings_user_sub ON portfolio.holdings (user_sub);
