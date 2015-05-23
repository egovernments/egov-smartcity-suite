DROP SEQUENCE seq_eis_employee;
CREATE SEQUENCE seq_egeis_employee;
ALTER TABLE eis_employee RENAME TO egeis_employee;
ALTER TABLE egeis_employee ADD COLUMN code VARCHAR(256);
ALTER TABLE egeis_employee ADD COLUMN dateofappointment DATE;
ALTER TABLE egeis_employee ADD COLUMN dateofretirement DATE;
ALTER TABLE egeis_employee ADD COLUMN pannumber VARCHAR(12);
ALTER TABLE egeis_employee ADD COLUMN employeestatus VARCHAR(16);
ALTER TABLE egeis_employee ADD COLUMN employeetype BIGINT;
ALTER TABLE egeis_employee ADD CONSTRAINT FK_EGEIS_EMPLOYEE_EMPTYPE FOREIGN KEY(employeetype)
REFERENCES egeis_employeetype(id);

--rollback DROP SEQUENCE seq_egeis_employee;
--rollback CREATE SEQUENCE seq_eis_employee;
--rollback ALTER TABLE egeis_employee RENAME TO eis_employee;
--rollback ALTER TABLE eis_employee DROP COLUMN code;
--rollback ALTER TABLE eis_employee DROP COLUMN dateofappointment;
--rollback ALTER TABLE eis_employee DROP COLUMN dateofretirement;
--rollback ALTER TABLE eis_employee DROP COLUMN pannumber;
--rollback ALTER TABLE eis_employee DROP COLUMN employeestatus;
--rollback ALTER TABLE eis_employee DROP COLUMN employeetype;

