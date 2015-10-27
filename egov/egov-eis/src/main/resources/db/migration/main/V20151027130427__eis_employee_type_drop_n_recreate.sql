DELETE FROM egeis_employeetype WHERE name = 'Permanent';

INSERT INTO egeis_employeetype (id, name, version, lastmodifieddate, createddate, createdby, lastmodifiedby, chartofaccounts) VALUES (nextval('seq_egeis_employeetype'), 'Permanent', 0, '2015-08-28 00:00:00', '2015-08-28 00:00:00', 1, 1, NULL);

UPDATE egeis_employee set employeetype = (select id from egeis_employeetype WHERE name = 'Permanent');