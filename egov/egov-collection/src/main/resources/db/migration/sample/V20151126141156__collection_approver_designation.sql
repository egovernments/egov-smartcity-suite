INSERT INTO eg_designation (id, name, description, chartofaccounts, version, createddate, lastmodifieddate, createdby, lastmodifiedby) VALUES (nextval('seq_eg_designation'), 'Manager', 'Manager', NULL, 0, now(), now(), 1, 1);

INSERT INTO egeis_deptdesig (id, designation, department, outsourcedposts, sanctionedposts, version, createddate, lastmodifieddate, createdby, lastmodifiedby) VALUES (nextval('seq_egeis_deptdesig'), (select id from eg_designation where name = 'Manager'), (select id from eg_department where name = 'Administration'), 0, 2, 0, now(), now(), 1, 1);

INSERT INTO eg_position (id, name, deptdesig, createddate, lastmodifieddate, createdby, lastmodifiedby, ispostoutsourced, version) VALUES (nextval('seq_eg_position'),'A-Manager',
(select id from egeis_deptdesig where designation = (select id from eg_designation where name = 'Manager') and department = (select id from eg_department where name = 'Administration')), now(), now(), 1, 1, false, 0);

INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,fromdate,todate,version,employee,isprimary) VALUES (nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Manager'),null,(SELECT id FROM eg_department WHERE name='Administration'),(SELECT id FROM eg_position WHERE name='A-Manager'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,(SELECT id FROM egeis_employee WHERE code='E063'),true);

INSERT INTO eg_userrole(userid,roleid) VALUES ((SELECT id FROM eg_user WHERE username='ravi'), (SELECT id FROM eg_role where name ='Remitter'));


