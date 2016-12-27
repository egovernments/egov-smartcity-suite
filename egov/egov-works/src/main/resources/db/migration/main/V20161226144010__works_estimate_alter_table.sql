ALTER TABLE egw_lineestimate ALTER COLUMN adminsanctionby TYPE character varying(100);
ALTER TABLE egw_mv_work_progress_register ALTER COLUMN adminsanctionby TYPE character varying(100);

--rollback ALTER TABLE egw_lineestimate ALTER COLUMN adminsanctionby TYPE bigint using adminsanctionby::bigint;
--rollback ALTER TABLE egw_mv_work_progress_register ALTER COLUMN adminsanctionby TYPE bigint using adminsanctionby::bigint;