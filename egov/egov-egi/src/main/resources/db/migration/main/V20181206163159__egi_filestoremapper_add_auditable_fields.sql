ALTER TABLE eg_filestoremap ADD COLUMN lastmodifieddate timestamp without time zone;
ALTER TABLE eg_filestoremap ADD COLUMN lastmodifiedby bigint;
ALTER TABLE eg_filestoremap ADD COLUMN createdby bigint;
UPDATE eg_filestoremap SET createddate=CURRENT_TIMESTAMP WHERE createddate is null;
UPDATE eg_filestoremap SET lastmodifieddate=createddate;
UPDATE eg_filestoremap SET createdby=(select id from eg_user where username='system');
UPDATE eg_filestoremap SET lastmodifiedby=(select id from eg_user where username='system');