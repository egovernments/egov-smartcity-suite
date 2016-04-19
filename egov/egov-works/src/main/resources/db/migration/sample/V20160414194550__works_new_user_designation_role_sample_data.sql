---------------Create new designation-----------------
INSERT INTO eg_designation(id, name, description, chartofaccounts, version, createddate, lastmodifieddate, createdby, lastmodifiedby, code) VALUES (nextval('seq_eg_designation'), 'Work Inspector/ Technical Mastry', 'Work Inspector/ Technical Mastry', null,0, now(), now(), 1, 1, 'WI');

---------------Map dept to designation----------------
INSERT INTO egeis_deptdesig(id, designation, department, outsourcedposts, sanctionedposts, version, createddate, lastmodifieddate, createdby, lastmodifiedby) VALUES (nextval('seq_egeis_deptdesig'), (select id from eg_designation where name = 'Work Inspector/ Technical Mastry'), (select id from eg_department where name = 'Engineering'), 0, 1, 0, now(), now(), 1, 1);

---------------Create new position--------------------
INSERT INTO eg_position(name, id, deptdesig, createddate, lastmodifieddate, createdby, lastmodifiedby, ispostoutsourced, version) VALUES ('WI-Work Inspector/ Technical Mastry-01', nextval('seq_eg_position'), (select id from egeis_deptdesig where designation = (select id from eg_designation where name = 'Work Inspector/ Technical Mastry') and department = (select id from eg_department where name = 'Engineering')), now(), now(), 1, 1, false, 0);

---------------Create new User----------------
INSERT INTO eg_user(id, title, salutation, dob, locale, username, password, pwdexpirydate, mobilenumber, altcontactnumber, emailid, createddate, lastmodifieddate, createdby, lastmodifiedby, active, name, gender, pan, aadhaarnumber, type, version, guardian, guardianrelation, signature) VALUES (nextval('seq_eg_user'), '', '', '1990-10-23 00:00:00', 'en_IN', 'ritesh', '$2a$10$uheIOutTnD33x7CDqac1zOL8DMiuz7mWplToPgcf7oxAI9OzRKxmK', '2099-01-01 00:00:00', '', '', '', '2016-03-30 00:00:00', '2016-03-30 00:00:00', 1, 1, TRUE, 'ritesh', 1, '', '', 'EMPLOYEE', 0, '', '', '<binary data>');

---------------Create new Employee----------------
INSERT INTO egeis_employee(id, code, dateofappointment, dateofretirement, employeestatus, employeetype, version) VALUES ((select id from eg_user where username = 'ritesh'), 'E192', '2016-03-29 00:00:00', '2099-01-01 00:00:00', 'EMPLOYED', 3, 0);

---------------Create new Assignment--------------------
INSERT INTO egeis_assignment(id, fund, function, designation, functionary, department, "position", grade, lastmodifiedby, lastmodifieddate, createddate, createdby, fromdate, todate, version, employee, isprimary) VALUES (nextval('seq_egeis_assignment'), null, null, (select id from eg_designation where name = 'Work Inspector/ Technical Mastry'), null, (select id from eg_department where name = 'Engineering'), (select id from eg_position where name = 'WI-Work Inspector/ Technical Mastry-01'), null, 1, now(), now(), 1, now(), '2016-12-31', 0, (select id from egeis_employee where id = (select id from eg_user where username = 'ritesh')), true);

---------------Map Users to Role--------------------
INSERT INTO eg_userrole(roleid, userid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_user where username = 'ritesh'));

INSERT INTO eg_userrole(roleid, userid) VALUES ((select id from eg_role where name = 'Works Approver'), (select id from eg_user where username = 'ritesh'));
INSERT INTO eg_userrole(roleid, userid) VALUES ((select id from eg_role where name = 'Employee'), (select id from eg_user where username = 'ritesh'));

--rollback delete from eg_userrole where roleid = (select id from eg_role where name = 'Employee') and userid = (select id from eg_user where username = 'ritesh');
--rollback delete from eg_userrole where roleid = (select id from eg_role where name = 'Works Approver') and userid = (select id from eg_user where username = 'ritesh');
--rollback delete from eg_userrole where roleid = (select id from eg_role where name = 'Works Creator') and userid = (select id from eg_user where username = 'ritesh');

--rollback delete from egeis_assignment where designation = (select id from eg_designation where name = 'Work Inspector/ Technical Mastry') and employee = (select id from egeis_employee where id = (select id from eg_user where username = 'ritesh'));
--rollback delete from egeis_employee where id = (select id from eg_user where username = 'ritesh');
--rollback delete from eg_user where username = 'ritesh';
--rollback delete from eg_position where name = 'WI-Work Inspector/ Technical Mastry-01';
--rollback delete from egeis_deptdesig where designation = (select id from eg_designation where name = 'Work Inspector/ Technical Mastry');
--rollback delete from eg_designation where name = 'Work Inspector/ Technical Mastry';