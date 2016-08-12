---------------Create new designation-----------------
INSERT INTO eg_designation(id, name, description, chartofaccounts, version, createddate, lastmodifieddate, createdby, lastmodifiedby, code) VALUES (nextval('seq_eg_designation'), 'Deputy executive engineer', 'Deputy executive engineer', null,0, now(), now(), 1, 1, 'DE');

---------------Map dept to designation----------------
INSERT INTO egeis_deptdesig(id, designation, department, outsourcedposts, sanctionedposts, version, createddate, lastmodifieddate, createdby, lastmodifiedby) VALUES (nextval('seq_egeis_deptdesig'), (select id from eg_designation where name = 'Deputy executive engineer'), (select id from eg_department where name = 'Engineering'), 0, 1, 0, now(), now(), 1, 1);


---------------Create new position--------------------
INSERT INTO eg_position(name, id, deptdesig, createddate, lastmodifieddate, createdby, lastmodifiedby, ispostoutsourced, version) VALUES ('DE-Deputy executive engineer-1', nextval('seq_eg_position'), (select id from egeis_deptdesig where designation = (select id from eg_designation where name = 'Deputy executive engineer') and department = (select id from eg_department where name = 'Engineering')), now(), now(), 1, 1, false, 0);

---------------Create new Assignment--------------------
INSERT INTO egeis_assignment(id, fund, function, designation, functionary, department, "position", grade, lastmodifiedby, lastmodifieddate, createddate, createdby, fromdate, todate, version, employee, isprimary) VALUES (nextval('seq_egeis_assignment'), null, null, (select id from eg_designation where name = 'Deputy executive engineer'), null, (select id from eg_department where name = 'Engineering'), (select id from eg_position where name = 'DE-Deputy executive engineer-1'), null, 1, now(), now(), 1, now(), '2016-12-31', 0, (select id from egeis_employee where id = (select id from eg_user where username = 'sathish')), true);


---------------Map Users to Role--------------------
INSERT INTO eg_userrole(roleid, userid) VALUES ((select id from eg_role where name = 'Sewerage Tax Approver'), (select id from eg_user where username = 'sathish'));
INSERT INTO eg_userrole(roleid, userid) VALUES ((select id from eg_role where name = 'Employee'), (select id from eg_user where username = 'sathish'));

INSERT INTO eg_userrole(roleid, userid) VALUES ((select id from eg_role where name = 'Sewerage Tax Approver'), (select id from eg_user where username = 'rishi'));

INSERT INTO eg_userrole(roleid, userid) VALUES ((select id from eg_role where name = 'Sewerage Tax Approver'), (select id from eg_user where username = 'vasanth'));

--rollback delete from eg_userrole where roleid = (select id from eg_role where name = 'Employee') and userid = (select id from eg_user where username = 'vasanth');
--rollback delete from eg_userrole where roleid = (select id from eg_role where name = 'Sewerage Tax Approver') and userid = (select id from eg_user where username = 'vasanth');
--rollback delete from eg_userrole where roleid = (select id from eg_role where name = 'Employee') and userid = (select id from eg_user where username = 'rishi');
--rollback delete from eg_userrole where roleid = (select id from eg_role where name = 'Sewerage Tax Approver') and userid = (select id from eg_user where username = 'rishi');
--rollback delete from eg_userrole where roleid = (select id from eg_role where name = 'Employee') and userid = (select id from eg_user where username = 'sathish');
--rollback delete from eg_userrole where roleid = (select id from eg_role where name = 'Sewerage Tax Approver') and userid = (select id from eg_user where username = 'sathish');
--rollback delete from egeis_assignment where designation = (select id from eg_designation where name = 'Deputy executive engineer') and employee = (select id from egeis_employee where id = (select id from eg_user where username = 'sathish'));
--rollback delete from eg_position where name = 'DE-Deputy executive engineer-1';
--rollback delete from egeis_deptdesig where designation = (select id from eg_designation where name = 'Deputy executive engineer');
--rollback delete from eg_designation where name = 'Deputy executive engineer';