-- Creates all service schemas and the app user if needed.
-- Flyway migrations in each service create the tables; this file just ensures schemas exist.

CREATE SCHEMA IF NOT EXISTS budget;
CREATE SCHEMA IF NOT EXISTS activity_log;
CREATE SCHEMA IF NOT EXISTS goals;
CREATE SCHEMA IF NOT EXISTS ledger;
CREATE SCHEMA IF NOT EXISTS portfolio;
CREATE SCHEMA IF NOT EXISTS recurring;
