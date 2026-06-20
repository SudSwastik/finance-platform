-- Creates all service schemas upfront.
-- Flyway migrations in each service create the tables.

CREATE SCHEMA IF NOT EXISTS identity;
CREATE SCHEMA IF NOT EXISTS budget;
CREATE SCHEMA IF NOT EXISTS finance;
CREATE SCHEMA IF NOT EXISTS portfolio;
