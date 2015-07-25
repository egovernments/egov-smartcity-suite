INSERT INTO eg_designation(id,name,description,chartofaccounts,version,createddate,lastmodifieddate,createdby,lastmodifiedby) VALUES
(nextval('seq_eg_designation'),'Revenue Inspector','Revenue Inspector',null,0,now(),now(),1,1);

INSERT INTO eg_designation(id,name,description,chartofaccounts,version,createddate,lastmodifieddate,createdby,lastmodifiedby) VALUES
(nextval('seq_eg_designation'),'Revenue Clerk','Revenue Clerk',null,0,now(),now(),1,1);

--EGEIS_DEPTDESIG
INSERT INTO egeis_deptdesig(id,designation,department,outsourcedposts,sanctionedposts,version,createddate,lastmodifieddate,createdby,lastmodifiedby)
values(nextval('seq_egeis_deptdesig'),(SELECT id FROM eg_designation where name='Revenue Inspector'),(SELECT id FROM eg_department WHERE name='Revenue'),0,2,0,now(),now(),1,1);

INSERT INTO egeis_deptdesig(id,designation,department,outsourcedposts,sanctionedposts,version,createddate,lastmodifieddate,createdby,lastmodifiedby)
values(nextval('seq_egeis_deptdesig'),(SELECT id FROM eg_designation where name='Revenue Clerk'),(SELECT id FROM eg_department WHERE name='Revenue'),0,2,0,now(),now(),1,1);

--EG_POSITION
INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-Revenue Inspector-1',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Revenue Inspector')),now(),now(),1,1,false,0);

INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-Revenue Inspector-2',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Revenue Inspector')),now(),now(),1,1,false,0);

INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-Revenue Clerk-1',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Revenue Clerk')),now(),now(),1,1,false,0);

INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-Revenue Clerk-2',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Revenue Clerk')),now(),now(),1,1,false,0);

--EGEIS_ASSIGNMENT
INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Revenue Inspector'),null,(SELECT id FROM eg_department WHERE name='Revenue'),
(SELECT id FROM eg_position WHERE name='R-Revenue Inspector-1'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E043'),true);

update egeis_assignment set designation = (select id from eg_designation  where name = 'Revenue Clerk'), position = (SELECT id FROM eg_position WHERE name='R-Revenue Clerk-1') 
WHERE employee in (SELECT id FROM egeis_employee   where code in ('E040'));

insert into eg_userrole (userid,roleid)
values( (select id from eg_user where username='gireesh'),(select id from eg_role where name='Property Verifier'));

--rollback delete from eg_userrole where userid = (select id from eg_user where username='gireesh') and roleid = (select id from eg_role where name='Property Verifier');
--rollback update egeis_assignment set designation = (select id from eg_designation  where name = 'Operator'), position = (SELECT id FROM eg_position WHERE name='R-Operator-1') WHERE employee in (SELECT id FROM egeis_employee   where code in ('E040'));
--rollback delete from egeis_assignment where designation = (SELECT id FROM eg_designation WHERE name='Revenue Inspector') and position = (SELECT id FROM eg_position WHERE name='R-Revenue Inspector-1') and employee = (SELECT id FROM egeis_employee WHERE code='E043');
--rollback delete from eg_position where name in ('R-Revenue Inspector-1', 'R-Revenue Inspector-2', 'R-Revenue Clerk-1', 'R-Revenue Clerk-2');
--rollback delete from egeis_deptdesig where designation = (SELECT id FROM eg_designation where name='Revenue Inspector') and department = (SELECT id FROM eg_department WHERE name='Revenue');
--rollback delete from egeis_deptdesig where designation = (SELECT id FROM eg_designation where name='Revenue Clerk') and department = (SELECT id FROM eg_department WHERE name='Revenue');
--rollback delete from eg_designation where name in ('Revenue Inspector', 'Revenue Clerk');