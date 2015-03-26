CREATE SEQUENCE seq_eg_hierarchy_type;

ALTER TABLE eg_heirarchy_type RENAME TO eg_hierarchy_type;

ALTER TABLE eg_hierarchy_type RENAME column id_heirarchy_type to id;
ALTER TABLE eg_hierarchy_type RENAME column type_name to name;
ALTER TABLE eg_hierarchy_type RENAME column type_code to code;

ALTER TABLE eg_hierarchy_type DROP column updatedtime;

ALTER TABLE eg_hierarchy_type ADD COLUMN createddate TIMESTAMP WITHOUT TIME ZONE;
ALTER TABLE eg_hierarchy_type ADD COLUMN lastmodifieddate TIMESTAMP WITHOUT TIME ZONE;
ALTER TABLE eg_hierarchy_type ADD COLUMN createdby BIGINT;
ALTER TABLE eg_hierarchy_type ADD COLUMN lastmodifiedby BIGINT;


-- ROLLBACK DROP SEQUENCE seq_eg_hierarchytype;

-- ROLLBACK ALTER TABLE egoverp.eg_hierarchy_type RENAME TO egoverp.eg_heirarchy_type;
-- ROLLBACK ALTER TABLE eg_hierarchy_type RENAME column id to id_heirarchy_type;
-- ROLLBACK ALTER TABLE eg_hierarchy_type RENAME column name to type_name;
-- ROLLBACK ALTER TABLE eg_hierarchy_type RENAME column code to type_code;

-- ROLLBACK ALTER TABLE eg_hierarchy_type DROP COLUMN createddate;
-- ROLLBACK ALTER TABLE eg_hierarchy_type DROP COLUMN lastmodifieddate;
-- ROLLBACK ALTER TABLE eg_hierarchy_type DROP COLUMN createdby;
-- ROLLBACK ALTER TABLE eg_hierarchy_type DROP COLUMN lastmodifiedby;




