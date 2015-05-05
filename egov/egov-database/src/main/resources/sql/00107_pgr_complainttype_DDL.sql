ALTER TABLE pgr_complainttype ADD COLUMN createddate TIMESTAMP WITHOUT TIME ZONE;
ALTER TABLE pgr_complainttype ADD COLUMN lastmodifieddate TIMESTAMP WITHOUT TIME ZONE;
ALTER TABLE pgr_complainttype ADD COLUMN createdby BIGINT;
ALTER TABLE pgr_complainttype ADD COLUMN lastmodifiedby BIGINT;

--roleback ALTER TABLE pgr_complainttype DROP COLUMN createddate;
--roleback ALTER TABLE pgr_complainttype DROP COLUMN lastmodifieddate;
--roleback ALTER TABLE pgr_complainttype DROP COLUMN createdby;
--roleback ALTER TABLE pgr_complainttype DROP COLUMN lastmodifiedby;