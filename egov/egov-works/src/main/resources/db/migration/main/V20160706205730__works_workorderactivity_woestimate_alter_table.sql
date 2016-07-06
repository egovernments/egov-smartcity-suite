------------------START------------------
ALTER TABLE egw_workorder_estimate ALTER COLUMN estimate_wo_amount TYPE double precision;
ALTER TABLE egw_workorder_activity ALTER COLUMN approved_quantity TYPE double precision;
ALTER TABLE egw_workorder_activity DROP COLUMN sorcategory;

--rollback ALTER TABLE egw_workorder_activity ALTER COLUMN approved_quantity TYPE bigint;
--rollback ALTER TABLE egw_workorder_estimate ALTER COLUMN estimate_wo_amount TYPE bigint;