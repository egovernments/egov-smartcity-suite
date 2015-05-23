DROP SEQUENCE SEQ_ASS_DEPT;
CREATE SEQUENCE seq_egeis_employee_hod;

ALTER TABLE eg_employee_dept RENAME TO egeis_employee_hod;
ALTER TABLE egeis_employee_hod RENAME COLUMN deptid TO department;
ALTER TABLE egeis_employee_hod RENAME COLUMN assignment_id TO assignment;

ALTER TABLE egeis_employee_hod ADD COLUMN version BIGINT;
ALTER TABLE egeis_employee_hod ADD COLUMN createddate timestamp without time zone;
ALTER TABLE egeis_employee_hod ADD COLUMN lastmodifieddate timestamp without time zone;
ALTER TABLE egeis_employee_hod ADD COLUMN createdby BIGINT;
ALTER TABLE egeis_employee_hod ADD COLUMN lastmodifiedby BIGINT;
UPDATE egeis_employee_hod SET lastmodifiedby=1, createdby=1, createddate='01-01-2015',lastmodifieddate='01-01-2015',version=0;

--rollback DROP SEQUENCE seq_egeis_employee_hod;
--rollback CREATE SEQ_ASS_DEPT;
--rollback ALTER TABLE RENAME egeis_employee_hod TO eg_employee_dept;
--rollback ALTER TABLE eg_employee_dept RENAME COLUMN department TO deptid;
--rollback ALTER TABLE eg_employee_dept  RENAME COLUMN assignment TO assignment_id;

--rollback ALTER TABLE eg_employee_dept DROP COLUMN version;
--rollback ALTER TABLE eg_employee_dept DROP COLUMN createddate;
--rollback ALTER TABLE eg_employee_dept DROP COLUMN lastmodifieddate;
--rollback ALTER TABLE eg_employee_dept DROP COLUMN createdby;
--rollback ALTER TABLE eg_employee_dept DROP COLUMN lastmodifiedby;
