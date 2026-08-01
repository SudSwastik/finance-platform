-- Optimistic locking: prevents a concurrent PATCH/contribute pair from silently
-- dropping one of the two updates.
ALTER TABLE goals.goals ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
