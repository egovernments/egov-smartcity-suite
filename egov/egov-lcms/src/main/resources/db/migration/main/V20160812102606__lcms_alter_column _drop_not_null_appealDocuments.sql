ALTER TABLE eglc_appeal_documents ALTER COLUMN documentname DROP not null;
---appeal_table
ALTER TABLE eglc_appeal DROP COLUMN createddate;
ALTER TABLE eglc_appeal DROP COLUMN createdby;
ALTER TABLE eglc_appeal DROP COLUMN lastmodifieddate;
ALTER TABLE eglc_appeal DROP COLUMN lastmodifiedby;

---contempt table
ALTER TABLE eglc_contempt DROP COLUMN createddate;
ALTER TABLE eglc_contempt DROP COLUMN createdby;
ALTER TABLE eglc_contempt DROP COLUMN lastmodifieddate;
ALTER TABLE eglc_contempt DROP COLUMN lastmodifiedby;