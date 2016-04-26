---------------Create new User----------------
INSERT INTO eg_user(id, title, salutation, dob, locale, username, password, pwdexpirydate, mobilenumber, altcontactnumber, emailid, createddate, lastmodifieddate, createdby, lastmodifiedby, active, name, gender, pan, aadhaarnumber, type, version, guardian, guardianrelation, signature) VALUES (nextval('seq_eg_user'), '', '', '1990-10-23 00:00:00', 'en_IN', 'manoj', '$2a$10$uheIOutTnD33x7CDqac1zOL8DMiuz7mWplToPgcf7oxAI9OzRKxmK', '2099-01-01 00:00:00', '', '', '', '2016-03-30 00:00:00', '2016-03-30 00:00:00', 1, 1, TRUE, 'manoj', 1, '', '', 'EMPLOYEE', 0, '', '', '<binary data>');

---------------Create new Employee----------------
INSERT INTO egeis_employee(id, code, dateofappointment, dateofretirement, employeestatus, employeetype, version) VALUES ((select id from eg_user where username = 'manoj'), 'E192', '2016-03-29 00:00:00', '2099-01-01 00:00:00', 'EMPLOYED', 3, 0);

---------------Create new position--------------------
INSERT INTO eg_position(name, id, deptdesig, createddate, lastmodifieddate, createdby, lastmodifiedby, ispostoutsourced, version) VALUES ('AE-Assistant engineer-01', nextval('seq_eg_position'), (select id from egeis_deptdesig where designation = (select id from eg_designation where name = 'Assistant engineer') and department = (select id from eg_department where name = 'Engineering')), now(), now(), 1, 1, false, 0);

---------------Create new Assignment--------------------
INSERT INTO egeis_assignment(id, fund, function, designation, functionary, department, "position", grade, lastmodifiedby, lastmodifieddate, createddate, createdby, fromdate, todate, version, employee, isprimary) VALUES (nextval('seq_egeis_assignment'), null, null, (select id from eg_designation where name = 'Assistant engineer'), null, (select id from eg_department where name = 'Engineering'), (select id from eg_position where name = 'AE-Assistant engineer-01'), null, 1, now(), now(), 1, now(), '2016-12-31', 0, (select id from egeis_employee where id = (select id from eg_user where username = 'manoj')), true);

---------------Map Users to Role--------------------
INSERT INTO eg_userrole(roleid, userid) VALUES ((select id from eg_role where name = 'Works Creator'), (select id from eg_user where username = 'manoj'));
INSERT INTO eg_userrole(roleid, userid) VALUES ((select id from eg_role where name = 'Employee'), (select id from eg_user where username = 'manoj'));

--rollback delete from eg_userrole where userid = (select id from eg_user where username = 'manoj');
--rollback delete from egeis_assignment where "position" = (select id from eg_position where name = 'AE-Assistant engineer-01');
--rollback delete from eg_position where name = 'AE-Assistant engineer-01';
--rollback delete from egeis_employee where id = (select id from eg_user where username = 'manoj');
--rollback delete from eg_user where username = 'manoj';