ALTER TABLE financialyear RENAME COLUMN created TO createddate;

ALTER TABLE financialyear RENAME COLUMN modifiedby TO lastmodifiedby;

ALTER TABLE financialyear RENAME COLUMN lastmodified TO lastmodifieddate;

ALTER TABLE financialyear  ADD COLUMN version numeric ;

ALTER TABLE financialyear ADD COLUMN createdby bigint;
