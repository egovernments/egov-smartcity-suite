DROP SEQUENCE SEQ_DESIGNATION;
CREATE SEQUENCE SEQ_EG_DESIGNATION;
ALTER TABLE eg_designation  RENAME COLUMN designation_name TO name;
ALTER TABLE eg_designation  RENAME COLUMN designation_description TO description;
ALTER TABLE eg_designation  RENAME COLUMN glcodeid TO chartofaccounts;
ALTER TABLE eg_designation RENAME COLUMN designationid TO id;

ALTER TABLE eg_designation ADD COLUMN version BIGINT;
ALTER TABLE eg_designation ADD COLUMN createddate timestamp without time zone;
ALTER TABLE eg_designation ADD COLUMN lastmodifieddate timestamp without time zone;
ALTER TABLE eg_designation ADD COLUMN createdby BIGINT;
ALTER TABLE eg_designation ADD COLUMN lastmodifiedby BIGINT;
UPDATE eg_designation SET lastmodifiedby=1, createdby=1, createddate='01-01-2015',lastmodifieddate='01-01-2015',version=0;

--rollback DROP SEQUENCE SEQ_EG_DESIGNATION;
--rollback CREATE SEQUENCE SEQ_DESIGNATION;
--rollback ALTER TABLE eg_designation RENAME COLUMN id TO designationid;
--rollback ALTER TABLE eg_designation  RENAME COLUMN name TO designation_name;
--rollback ALTER TABLE eg_designation  RENAME COLUMN description TO designation_description;
--rollback ALTER TABLE eg_designation  RENAME COLUMN chartofaccounts TO glcodeid;

--rollback ALTER TABLE eg_designation DROP COLUMN version;
--rollback ALTER TABLE eg_designation DROP COLUMN createddate;
--rollback ALTER TABLE eg_designation DROP COLUMN lastmodifieddate;
--rollback ALTER TABLE eg_designation DROP COLUMN createdby;
--rollback ALTER TABLE eg_designation DROP COLUMN lastmodifiedby;
