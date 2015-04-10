create sequence seq_eis_employee;

SELECT setval('seq_eis_employee', (select max(id) from eg_user)); 

CREATE TABLE eis_employee
(
  id numeric NOT NULL
);



--rollback DROP TABLE eis_employee;
--rollback DROP SEQUENCE seq_eis_employee;

