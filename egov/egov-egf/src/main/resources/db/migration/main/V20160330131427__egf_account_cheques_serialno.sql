ALTER TABLE egf_Account_cheques
    ALTER COLUMN serialno TYPE bigint USING serialno::bigint;
