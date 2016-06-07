----------------------Adding new columns to line estimate table-------------------------
ALTER TABLE egw_lineestimate ADD COLUMN adminsanctionnumber varchar(50);
ALTER TABLE egw_lineestimate ADD COLUMN adminsanctiondate timestamp without time zone;
ALTER TABLE egw_lineestimate ADD COLUMN adminsanctionby bigint;

--rollback ALTER TABLE egw_lineestimate DROP COLUMN adminsanctionby;
--rollback ALTER TABLE egw_lineestimate DROP COLUMN adminsanctiondate;
--rollback ALTER TABLE egw_lineestimate DROP COLUMN adminsanctionnumber;