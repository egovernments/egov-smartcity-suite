ALTER TABLE egw_contractor_detail ADD COLUMN category character varying(100);

--rollback ALTER TABLE egw_contractor_detail DROP COLUMN category;