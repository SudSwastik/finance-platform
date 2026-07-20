ALTER TABLE finance.transactions
    ADD COLUMN recurring_frequency VARCHAR(16) CHECK (recurring_frequency IN ('WEEKLY','MONTHLY','YEARLY')),
    ADD COLUMN next_due_date DATE;

UPDATE finance.transactions
    SET recurring_frequency = 'MONTHLY',
        next_due_date = (transaction_date + INTERVAL '1 month')::date
    WHERE is_recurring = true;
