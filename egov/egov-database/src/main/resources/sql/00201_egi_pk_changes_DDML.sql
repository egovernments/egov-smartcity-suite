DROP SEQUENCE SEQ_EGEIS_EMPLOYEE;
DROP SEQUENCE SEQ_EG_CITIZEN;

ALTER TABLE EG_CITY_WEBSITE ADD COLUMN createdby numeric;
ALTER TABLE EG_CITY_WEBSITE ADD COLUMN lastmodifiedby numeric;
ALTER TABLE EG_CITY_WEBSITE ADD COLUMN createddate timestamp;
ALTER TABLE EG_CITY_WEBSITE ADD COLUMN lastmodifieddate timestamp;

ALTER TABLE EG_MODULE DROP COLUMN "version";

ALTER SEQUENCE eg_wf_state_history_seq RENAME TO seq_eg_wf_state_history;

ALTER SEQUENCE seq_pgr_complainant RENAME TO seq_egpgr_complainant;
ALTER TABLE pgr_complainant RENAME TO egpgr_complainant;
ALTER SEQUENCE SEQ_PGR_COMPLAINT RENAME TO SEQ_EGPGR_COMPLAINT;
ALTER TABLE pgr_complaint RENAME TO egpgr_complaint;
ALTER SEQUENCE seq_pgr_router RENAME TO seq_egpgr_router;
ALTER TABLE pgr_router RENAME TO egpgr_router;
ALTER SEQUENCE seq_pgr_complaintstatus RENAME TO seq_egpgr_complaintstatus;
ALTER TABLE pgr_complaintstatus RENAME TO egpgr_complaintstatus;
ALTER TABLE PGR_COMPLAINTSTATUS_MAPPING RENAME TO EGPGR_COMPLAINTSTATUS_MAPPING;
ALTER SEQUENCE SEQ_PGR_COMPLAINTSTATUS_MAPPING RENAME TO SEQ_EGPGR_COMPLAINTSTATUS_MAPPING;
ALTER TABLE PGR_ESCALATION RENAME TO EGPGR_ESCALATION;
ALTER SEQUENCE SEQ_PGR_ESCALATION RENAME TO SEQ_EGPGR_ESCALATION;
ALTER TABLE PGR_RECEIVING_CENTER RENAME TO EGPGR_RECEIVING_CENTER;
ALTER SEQUENCE SEQ_PGR_RECEIVING_CENTER RENAME TO SEQ_EGPGR_RECEIVING_CENTER;

ALTER TABLE EG_USER ADD COLUMN "version" numeric;
alter table eg_user alter column "type" type varchar(50) USING ("type"::varchar);
update eg_user set "type"='CITIZEN' where "type"='1';
update eg_user set "type"='EMPLOYEE' where "type"='2';
update eg_user set "type" = 'EMPLOYEE' where username='anonymous';

ALTER TABLE EG_USER ADD COLUMN "version" numeric default 0;
ALTER TABLE EG_EMPLOYEE ADD COLUMN "version" numeric default 0;
ALTER TABLE EG_CITIZEN ADD COLUMN "version" numeric default 0;
