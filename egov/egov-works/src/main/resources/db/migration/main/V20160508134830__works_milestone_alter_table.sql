ALTER TABLE egw_milestone ADD COLUMN version bigint DEFAULT 0;
ALTER TABLE egw_milestone RENAME COLUMN WORKORDER_ESTIMATE_ID TO workorderestimate;
ALTER TABLE egw_milestone RENAME COLUMN document_number TO documentnumber;
ALTER TABLE egw_milestone RENAME COLUMN status_id TO status;
ALTER TABLE egw_milestone RENAME COLUMN APPROVED_DATE TO approveddate;
ALTER TABLE egw_milestone RENAME COLUMN created_by TO createdby;
ALTER TABLE egw_milestone RENAME COLUMN created_date TO createddate;
ALTER TABLE egw_milestone RENAME COLUMN modified_by TO lastmodifiedby;
ALTER TABLE egw_milestone RENAME COLUMN modified_date TO lastModifieddate;

ALTER TABLE egw_milestone_activity DROP COLUMN milestone_activity_index;
ALTER TABLE egw_milestone_activity ADD COLUMN version bigint DEFAULT 0;
ALTER TABLE egw_milestone_activity RENAME COLUMN stage_order_no TO stageorderno;
ALTER TABLE egw_milestone_activity ALTER COLUMN stageorderno TYPE double precision;
ALTER TABLE egw_milestone_activity RENAME COLUMN milestone_id TO milestone;
ALTER TABLE egw_milestone_activity RENAME COLUMN modifiedby TO lastmodifiedby;
ALTER TABLE egw_milestone_activity RENAME COLUMN modifieddate TO lastModifieddate;

ALTER TABLE egw_milestone_activity ADD COLUMN schedulestartdate timestamp without time zone;
ALTER TABLE egw_milestone_activity ADD COLUMN scheduleenddate timestamp without time zone;

--rollback ALTER TABLE egw_milestone_activity DROP COLUMN version;
--rollback ALTER TABLE egw_milestone_activity ADD COLUMN milestone_activity_index bigint DEFAULT 0;
--rollback ALTER TABLE egw_milestone_activity RENAME COLUMN stageorderno TO stage_order_no;
--rollback ALTER TABLE egw_milestone_activity RENAME COLUMN milestone TO milestone_id;
--rollback ALTER TABLE egw_milestone_activity RENAME COLUMN lastmodifiedby TO modifiedby;
--rollback ALTER TABLE egw_milestone_activity RENAME COLUMN lastmodifieddate TO modifieddate;
--rollback ALTER TABLE egw_milestone_activity DROP COLUMN schedulestartdate;
--rollback ALTER TABLE egw_milestone_activity DROP COLUMN scheduleenddate;

--rollback ALTER TABLE EGW_MILESTONE DROP COLUMN version;
--rollback ALTER TABLE egw_milestone RENAME COLUMN workorderestimate TO WORKORDER_ESTIMATE_ID;
--rollback ALTER TABLE egw_milestone RENAME COLUMN documentnumber TO document_number;
--rollback ALTER TABLE egw_milestone RENAME COLUMN approveddate TO APPROVED_DATE;
--rollback ALTER TABLE egw_milestone RENAME COLUMN createdby TO created_by;
--rollback ALTER TABLE egw_milestone RENAME COLUMN createddate TO created_date;
--rollback ALTER TABLE egw_milestone RENAME COLUMN lastmodifiedby TO modified_by;
--rollback ALTER TABLE egw_milestone RENAME COLUMN lastmodifieddate TO modified_date;
--rollback ALTER TABLE egw_milestone RENAME COLUMN status TO status_id;