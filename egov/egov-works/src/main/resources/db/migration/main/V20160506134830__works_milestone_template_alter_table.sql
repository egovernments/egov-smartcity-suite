ALTER TABLE EGW_MILESTONE_TEMPLATE ALTER COLUMN status TYPE Integer;
ALTER TABLE EGW_MILESTONE_TEMPLATE ALTER COLUMN status set DEFAULT 0;

ALTER TABLE EGW_MILESTONE_TEMPLATE ADD COLUMN version bigint DEFAULT 0;
ALTER TABLE EGW_MILESTONE_TEMPLATE RENAME COLUMN created_by TO createdby;
ALTER TABLE EGW_MILESTONE_TEMPLATE RENAME COLUMN created_date TO createddate;
ALTER TABLE EGW_MILESTONE_TEMPLATE RENAME COLUMN modified_by TO lastmodifiedby;
ALTER TABLE EGW_MILESTONE_TEMPLATE RENAME COLUMN modified_date TO lastmodifieddate;
ALTER TABLE EGW_MILESTONE_TEMPLATE RENAME COLUMN worktype_id TO typeofwork;
ALTER TABLE EGW_MILESTONE_TEMPLATE RENAME COLUMN worksubtype_id TO subtypeofwork;

ALTER TABLE EGW_MILESTONE_TEMPLATE DROP COLUMN state_id;
ALTER TABLE EGW_MILESTONE_TEMPLATE DROP COLUMN status_id;

ALTER TABLE egw_milestone_templ_activity ADD COLUMN version bigint DEFAULT 0;
ALTER TABLE egw_milestone_templ_activity RENAME COLUMN milestone_template_id TO milestonetemplate;
ALTER TABLE egw_milestone_templ_activity RENAME COLUMN stage_order_no TO stageorderno;
ALTER TABLE egw_milestone_templ_activity ALTER COLUMN percentage TYPE double precision;
ALTER TABLE egw_milestone_templ_activity DROP COLUMN milestonetempl_activity_index;
ALTER TABLE egw_milestone_templ_activity RENAME COLUMN modifiedby TO lastmodifiedby;
ALTER TABLE egw_milestone_templ_activity RENAME COLUMN modifieddate TO lastmodifieddate;
ALTER TABLE egw_milestone_templ_activity ALTER COLUMN stageorderno TYPE double precision;

--rollback ALTER TABLE EGW_MILESTONE_TEMPLATE ALTER COLUMN status TYPE bigint;
--rollback ALTER TABLE EGW_MILESTONE_TEMPLATE DROP COLUMN version;
--rollback ALTER TABLE EGW_MILESTONE_TEMPLATE RENAME COLUMN createdBy TO created_by;
--rollback ALTER TABLE EGW_MILESTONE_TEMPLATE RENAME COLUMN createdDate TO created_date;
--rollback ALTER TABLE EGW_MILESTONE_TEMPLATE RENAME COLUMN lastModifiedBy TO modified_by;
--rollback ALTER TABLE EGW_MILESTONE_TEMPLATE RENAME COLUMN lastModifiedDate TO modified_date;
--rollback ALTER TABLE EGW_MILESTONE_TEMPLATE RENAME COLUMN typeofwork TO worktype_id;
--rollback ALTER TABLE EGW_MILESTONE_TEMPLATE RENAME COLUMN subtypeofwork TO worksubtype_id;

--rollback ALTER TABLE EGW_MILESTONE_TEMPLATE ADD COLUMN state_id bigint DEFAULT 0;
--rollback ALTER TABLE EGW_MILESTONE_TEMPLATE ADD COLUMN status_id bigint DEFAULT 0;

--rollback ALTER TABLE egw_milestone_templ_activity DROP COLUMN version;
--rollback ALTER TABLE egw_milestone_templ_activity ADD COLUMN milestonetempl_activity_index bigint DEFAULT 0;
--rollback ALTER TABLE egw_milestone_templ_activity ALTER COLUMN percentage TYPE bigint;
--rollback ALTER TABLE egw_milestone_templ_activity RENAME COLUMN stageorderno TO STAGE_ORDER_NO;
--rollback ALTER TABLE egw_milestone_templ_activity RENAME COLUMN lastModifiedBy TO modifiedby;
--rollback ALTER TABLE egw_milestone_templ_activity RENAME COLUMN lastModifiedDate TO modifieddate;
--rollback ALTER TABLE egw_milestone_templ_activity RENAME COLUMN milestonetemplate TO milestone_template_id;
