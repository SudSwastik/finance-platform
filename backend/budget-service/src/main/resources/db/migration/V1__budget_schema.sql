CREATE SCHEMA IF NOT EXISTS budget;

CREATE TABLE budget.user_budget_summary (
    user_sub       VARCHAR(128) PRIMARY KEY,
    total_display  NUMERIC(19, 2) NOT NULL
);

CREATE TABLE budget.budget_categories (
    id           UUID PRIMARY KEY,
    user_sub     VARCHAR(128) NOT NULL,
    name         VARCHAR(128) NOT NULL,
    color_token  VARCHAR(64) NOT NULL,
    spent        NUMERIC(19, 2) NOT NULL,
    budget_cap   NUMERIC(19, 2) NOT NULL
);

CREATE INDEX idx_budget_categories_user_sub ON budget.budget_categories (user_sub);
