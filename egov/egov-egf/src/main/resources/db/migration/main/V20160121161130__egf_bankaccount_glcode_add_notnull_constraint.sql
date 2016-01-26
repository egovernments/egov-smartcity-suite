ALTER TABLE bankaccount ALTER COLUMN glcodeid SET NOT NULL;

ALTER TABLE bankaccount DROP COLUMN currentbalance RESTRICT;
