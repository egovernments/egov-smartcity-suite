ALTER TABLE egw_abstractestimate ADD COLUMN workcategory character varying(100);

--rollback ALTER TABLE egw_abstractestimate DROP COLUMN workcategory;
