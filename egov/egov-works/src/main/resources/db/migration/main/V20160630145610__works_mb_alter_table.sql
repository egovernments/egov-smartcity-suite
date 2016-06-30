------------------START------------------
ALTER TABLE egw_mb_header ADD COLUMN MB_ISSUED_DATE timestamp without time zone;

--rollback ALTER TABLE egw_mb_header DROP COLUMN MB_ISSUED_DATE;