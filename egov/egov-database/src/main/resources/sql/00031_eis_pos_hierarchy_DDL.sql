ALTER TABLE eg_position_hir ADD COLUMN object_sub_type VARCHAR(512);

ALTER TABLE eg_position_hir ADD COLUMN lastmodifiedby bigint;

ALTER TABLE eg_position_hir ADD lastmodifieddate timestamp without time zone;

ALTER TABLE eg_position_hir ADD createddate timestamp without time zone;

ALTER TABLE eg_position_hir ADD createdby bigint;

ALTER TABLE eg_position_hir RENAME TO egeis_position_hierarchy;

--rollback ALTER TABLE eg_position_hir DROP COLUMN object_sub_type;
--rollback ALTER TABLE eg_position_hir DROP COLUMN lastmodifiedby;
--rollback ALTER TABLE eg_position_hir DROP COLUMN lastmodifieddate;
--rollback ALTER TABLE eg_position_hir DROP COLUMN createdby;
--rollback ALTER TABLE eg_position_hir DROP COLUMN lastmodifiedby;
--rollback ALTER TABLE egeis_position_hierarchy RENAME TO eg_position_hir;

