------------------START------------------
ALTER TABLE egw_overhead ADD COLUMN version bigint DEFAULT 0;
ALTER TABLE egw_overhead DROP COLUMN expenditure_type;
ALTER TABLE egw_overhead RENAME COLUMN accountcode_id TO accountcode;
ALTER TABLE egw_overhead RENAME COLUMN created_by TO createdby;
ALTER TABLE egw_overhead RENAME COLUMN created_date TO createddate;
ALTER TABLE egw_overhead RENAME COLUMN modified_by TO lastmodifiedby;
ALTER TABLE egw_overhead RENAME COLUMN modified_date TO lastModifieddate;

ALTER TABLE egw_overhead_rate RENAME COLUMN overhead_id TO overhead;
ALTER TABLE egw_overhead_rate RENAME COLUMN lumpsum_amount TO lumpsumAmount;
ALTER TABLE egw_overhead_rate DROP COLUMN my_ohr_index;
ALTER TABLE egw_overhead_rate ADD COLUMN version bigint DEFAULT 0;
ALTER TABLE egw_overhead_rate ADD COLUMN createdby bigint;
ALTER TABLE egw_overhead_rate ADD COLUMN lastmodifiedby bigint;
ALTER TABLE egw_overhead_rate ADD COLUMN createddate timestamp without time zone;
ALTER TABLE egw_overhead_rate ADD COLUMN lastModifieddate timestamp without time zone;

--rollback ALTER TABLE egw_overhead_rate RENAME COLUMN lumpsumAmount TO lumpsum_amount;
--rollback ALTER TABLE egw_overhead_rate RENAME COLUMN overhead TO overhead_id;
--rollback ALTER TABLE egw_overhead_rate ADD COLUMN my_ohr_index bigint DEFAULT 0;
--rollback ALTER TABLE egw_overhead_rate DROP COLUMN version;
--rollback ALTER TABLE egw_overhead DROP COLUMN createdby;
--rollback ALTER TABLE egw_overhead DROP COLUMN lastmodifiedby;
--rollback ALTER TABLE egw_overhead DROP COLUMN createddate;
--rollback ALTER TABLE egw_overhead DROP COLUMN lastModifieddate;

--rollback ALTER TABLE egw_overhead DROP COLUMN version;
--rollback ALTER TABLE egw_overhead ADD COLUMN expenditure_type varchar(256);
--rollback ALTER TABLE egw_overhead RENAME COLUMN accountcode TO accountcode_id;
--rollback ALTER TABLE egw_overhead RENAME COLUMN createdby TO created_by;
--rollback ALTER TABLE egw_overhead RENAME COLUMN createddate TO created_date;
--rollback ALTER TABLE egw_overhead RENAME COLUMN lastmodifiedby TO modified_by;
--rollback ALTER TABLE egw_overhead RENAME COLUMN lastmodifieddate TO modified_date;
