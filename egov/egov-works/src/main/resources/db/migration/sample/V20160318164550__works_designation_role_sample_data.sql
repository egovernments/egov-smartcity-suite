---------------Map dept to designation----------------
INSERT INTO egeis_deptdesig(id, designation, department, outsourcedposts, sanctionedposts, version, createddate, lastmodifieddate, createdby, lastmodifiedby) VALUES (nextval('seq_egeis_deptdesig'), (select id from eg_designation where name = 'Superintending engineer'), (select id from eg_department where name = 'Engineering'), 0, 1, 0, now(), now(), 1, 1);
INSERT INTO egeis_deptdesig(id, designation, department, outsourcedposts, sanctionedposts, version, createddate, lastmodifieddate, createdby, lastmodifiedby) VALUES (nextval('seq_egeis_deptdesig'), (select id from eg_designation where name = 'Superintendent'), (select id from eg_department where name = 'Accounts'), 0, 1, 0, now(), now(), 1, 1);

---------------Create new position--------------------
INSERT INTO eg_position(name, id, deptdesig, createddate, lastmodifieddate, createdby, lastmodifiedby, ispostoutsourced, version) VALUES ('SU-Superintending engineer-01', nextval('seq_eg_position'), (select id from egeis_deptdesig where designation = (select id from eg_designation where name = 'Superintending engineer') and department = (select id from eg_department where name = 'Engineering')), now(), now(), 1, 1, false, 0);
INSERT INTO eg_position(name, id, deptdesig, createddate, lastmodifieddate, createdby, lastmodifiedby, ispostoutsourced, version) VALUES ('AC-Superintendent-01', nextval('seq_eg_position'), (select id from egeis_deptdesig where designation = (select id from eg_designation where name = 'Superintendent') and department = (select id from eg_department where name = 'Accounts')), now(), now(), 1, 1, false, 0);

---------------Create new Assignment--------------------
INSERT INTO egeis_assignment(id, fund, function, designation, functionary, department, "position", grade, lastmodifiedby, lastmodifieddate, createddate, createdby, fromdate, todate, version, employee, isprimary) VALUES (nextval('seq_egeis_assignment'), null, null, (select id from eg_designation where name = 'Superintending engineer'), null, (select id from eg_department where name = 'Engineering'), (select id from eg_position where name = 'SU-Superintending engineer-01'), null, 1, now(), now(), 1, now(), '2016-12-31', 0, (select id from egeis_employee where id = (select id from eg_user where username = 'sathish')), true);
UPDATE egeis_assignment SET isprimary = false WHERE employee = (select id from egeis_employee where id = (select id from eg_user where username = 'shahid'));
INSERT INTO egeis_assignment(id, fund, function, designation, functionary, department, "position", grade, lastmodifiedby, lastmodifieddate, createddate, createdby, fromdate, todate, version, employee, isprimary) VALUES (nextval('seq_egeis_assignment'), null, null, (select id from eg_designation where name = 'Superintendent'), null, (select id from eg_department where name = 'Accounts'), (select id from eg_position where name = 'AC-Superintendent-01'), null, 1, now(), now(), 1, now(), '2016-12-31', 0, (select id from egeis_employee where id = (select id from eg_user where username = 'shahid')), true);

---------------Map Users to Role--------------------
INSERT INTO eg_userrole(roleid, userid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_user where username = 'suresh'));

INSERT INTO eg_userrole(roleid, userid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_user where username = 'sathish'));
INSERT INTO eg_userrole(roleid, userid) VALUES ((select id from eg_role where name = 'Employee'), (select id from eg_user where username = 'sathish'));

INSERT INTO eg_userrole(roleid, userid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_user where username = 'shahid'));

INSERT INTO eg_userrole(roleid, userid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_user where username = 'vaibhav'));

--rollback delete from eg_userrole where roleid = (select id from eg_role where name = 'Employee') and userid = (select id from eg_user where username = 'sathish');
--rollback delete from eg_userrole where roleid = (select id from eg_role where name = 'Works Approver') and userid = (select id from eg_user where username = 'vaibhav');
--rollback delete from eg_userrole where roleid = (select id from eg_role where name = 'Works Approver') and userid = (select id from eg_user where username = 'shahid');
--rollback delete from eg_userrole where roleid = (select id from eg_role where name = 'Works Approver') and userid = (select id from eg_user where username = 'sathish');
--rollback delete from eg_userrole where roleid = (select id from eg_role where name = 'Works Creator') and userid = (select id from eg_user where username = 'narasappa');

--rollback delete from egeis_assignment where designation = (select id from eg_designation where name = 'Superintendent') and employee = (select id from egeis_employee where id = (select id from eg_user where username = 'shahid'));
--rollback UPDATE egeis_assignment SET isprimary = true WHERE employee = (select id from egeis_employee where id = (select id from eg_user where username = 'shahid'));
--rollback delete from egeis_assignment where designation = (select id from eg_designation where name = 'Superintending engineer') and employee = (select id from egeis_employee where id = (select id from eg_user where username = 'sathish'));
--rollback delete from eg_position where name = 'AC-Superintendent-01';
--rollback delete from eg_position where name = 'SU-Superintending engineer-01';
--rollback delete from egeis_deptdesig where designation = (select id from eg_designation where name = 'Superintending engineer');
--rollback delete from egeis_deptdesig where designation = (select id from eg_designation where name = 'Superintendent');