ALTER TABLE pgr_complainttype ADD COLUMN CODE VARCHAR(20);
--Rollback ALTER TABLE pgr_complainttype DROP COLUMN CODE ;

ALTER TABLE pgr_complainttype ADD COLUMN ISACTIVE BOOLEAN;
--Rollback ALTER TABLE pgr_complainttype DROP COLUMN ISACTIVE;