ALTER TABLE eg_address ADD COLUMN version bigint;
ALTER TABLE eg_event_processor_spec ADD COLUMN version bigint;
ALTER TABLE eg_event_result ADD COLUMN version bigint;
ALTER TABLE eg_filestoremap ADD COLUMN version bigint;
ALTER TABLE eg_hierarchy_type ADD COLUMN version bigint;
ALTER TABLE eg_role ADD COLUMN version bigint;
ALTER TABLE EG_WF_STATES ADD COLUMN version bigint;
ALTER TABLE EG_WF_TYPES ADD COLUMN version bigint;
ALTER TABLE eg_user ADD COLUMN version bigint;

ALTER TABLE pgr_complainant ADD COLUMN version bigint;
ALTER TABLE pgr_complaintstatus ADD COLUMN version bigint;
ALTER TABLE pgr_complaintstatus_mapping ADD COLUMN version bigint;
ALTER TABLE pgr_complainttype ADD COLUMN version bigint;
ALTER TABLE pgr_receiving_center ADD COLUMN version bigint;
ALTER TABLE pgr_escalation ADD COLUMN version bigint;
ALTER TABLE pgr_complaint ADD COLUMN version bigint;

ALTER TABLE egeis_position_hierarchy ADD COLUMN version bigint;
ALTER TABLE VOUCHERHEADER ADD COLUMN version bigint;
ALTER TABLE EGEIS_POST_CREATION ADD COLUMN version bigint;