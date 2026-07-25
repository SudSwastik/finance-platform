CREATE SCHEMA IF NOT EXISTS goals;

CREATE TABLE goals.goals (
    id             UUID PRIMARY KEY,
    tenant_id      UUID NOT NULL,
    user_sub       VARCHAR(128) NOT NULL,
    name           VARCHAR(128) NOT NULL,
    color_token    VARCHAR(64) NOT NULL,
    current_amount NUMERIC(19, 2) NOT NULL DEFAULT 0,
    target_amount  NUMERIC(19, 2) NOT NULL,
    target_date    DATE NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_goals_user_sub ON goals.goals (user_sub);
CREATE INDEX idx_goals_tenant_id ON goals.goals (tenant_id);

CREATE TABLE goals.goal_contributions (
    id             UUID PRIMARY KEY,
    goal_id        UUID NOT NULL REFERENCES goals.goals (id) ON DELETE CASCADE,
    tenant_id      UUID NOT NULL,
    user_sub       VARCHAR(128) NOT NULL,
    amount         NUMERIC(19, 2) NOT NULL,
    note           VARCHAR(256),
    contributed_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_goal_contributions_goal_id ON goals.goal_contributions (goal_id);
CREATE INDEX idx_goal_contributions_user_sub ON goals.goal_contributions (user_sub);
