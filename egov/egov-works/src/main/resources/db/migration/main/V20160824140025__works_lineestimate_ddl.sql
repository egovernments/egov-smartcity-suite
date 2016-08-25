ALTER TABLE EGW_LINEESTIMATE DROP COLUMN typeofslum;

--rollback ALTER TABLE egw_lineestimate ADD COLUMN typeofslum character varying(100) not null;