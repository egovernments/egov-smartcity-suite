INSERT INTO egeis_assignment (id, fund, function, designation, functionary, department, position, grade, 
lastmodifiedby, lastmodifieddate, createddate, createdby, fromdate, todate, version, employee, 
isprimary) VALUES (nextval('seq_egeis_assignment'), 1, NULL, (select id from eg_designation where name='Senior Assistant'),
 NULL, (select id from eg_department where name='Accounts'), 
 (select id from eg_position where name='R-Operator-1'), 1, 1, '2015-01-01 00:00:00', '2015-01-01 00:00:00', 1
, '2015-04-01', '2020-03-31', 0,( select id from eg_user where username='suresh'), true);