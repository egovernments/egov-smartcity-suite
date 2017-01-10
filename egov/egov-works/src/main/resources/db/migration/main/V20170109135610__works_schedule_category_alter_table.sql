------------------START------------------
ALTER TABLE egw_schedulecategory ADD COLUMN version bigint DEFAULT 0;
ALTER TABLE egw_schedulecategory RENAME COLUMN created_by TO createdby;
ALTER TABLE egw_schedulecategory RENAME COLUMN created_date TO createddate;
ALTER TABLE egw_schedulecategory RENAME COLUMN modified_by TO lastmodifiedby;
ALTER TABLE egw_schedulecategory RENAME COLUMN modified_date TO lastModifieddate;

--rollback ALTER TABLE egw_schedulecategory DROP COLUMN version;
--rollback ALTER TABLE egw_schedulecategory RENAME COLUMN createdby TO created_by;
--rollback ALTER TABLE egw_schedulecategory RENAME COLUMN createddate TO created_date;
--rollback ALTER TABLE egw_schedulecategory RENAME COLUMN lastmodifiedby TO modified_by;
--rollback ALTER TABLE egw_schedulecategory RENAME COLUMN lastmodifieddate TO modified_date;
