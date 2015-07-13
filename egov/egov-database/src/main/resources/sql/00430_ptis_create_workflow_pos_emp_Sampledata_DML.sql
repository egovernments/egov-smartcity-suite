--EGEIS_DEPTDESIG
INSERT INTO egeis_deptdesig(id,designation,department,outsourcedposts,sanctionedposts,version,createddate,lastmodifieddate,createdby,lastmodifiedby)
values(nextval('seq_egeis_deptdesig'),(SELECT id FROM eg_designation where name='Operator'),(SELECT id FROM eg_department WHERE name='Revenue'),0,2,0,now(),now(),1,1);

INSERT INTO egeis_deptdesig(id,designation,department,outsourcedposts,sanctionedposts,version,createddate,lastmodifieddate,createdby,lastmodifiedby)
values(nextval('seq_egeis_deptdesig'),(SELECT id FROM eg_designation where name='Bill Collector'),(SELECT id FROM eg_department WHERE name='Revenue'),0,2,0,now(),now(),1,1);


--EG_POSITION
INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-Operator-1',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Operator')),now(),now(),1,1,false,0);

INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-Operator-2',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Operator')),now(),now(),1,1,false,0);




INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-Bill Collector-1',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Bill Collector')),now(),now(),1,1,false,0);

INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-Bill Collector-2',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Bill Collector')),now(),now(),1,1,false,0);


--EGEIS_ASSIGNMENT
INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Operator'),null,(SELECT id FROM eg_department WHERE name='Revenue'),
(SELECT id FROM eg_position WHERE name='R-Operator-1'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E040'),true);



INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Bill Collector'),null,(SELECT id FROM eg_department WHERE name='Revenue'),
(SELECT id FROM eg_position WHERE name='R-Bill Collector-1'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E041'),true);


insert into eg_userrole (userid,roleid)
values( (select id from eg_user where username='chandrashekar'),(select id from eg_role where name='Property Approver'));


insert into eg_userrole (userid,roleid)
values( (select id from eg_user where username='mahesh'),(select id from eg_role where name='ULB Operator'));


--rollback DELETE FROM egeis_assignment WHERE employee in (SELECT id FROM egeis_employee   where code in ('E040','E041'));
--rollback DELETE FROM eg_position WHERE name in ('R-Operator-1','R-Operator-2','R-Bill Collector-1','R-Bill Collector-2','R-COMMISIONER-2');
--rollback DELETE FROM egeis_deptdesig where designation in (SELECT id FROM eg_designation where name='Operator') and department in (SELECT id FROM eg_department WHERE name='Revenue');
--rollback DELETE FROM egeis_deptdesig where designation in (SELECT id FROM eg_designation where name='Bill Collector') and department in (SELECT id FROM eg_department WHERE name='Revenue');
--rollback DELETE FROM egeis_deptdesig where designation in (SELECT id FROM eg_designation where name='Commissioner') and department in (SELECT id FROM eg_department WHERE name='Revenue');
--rollback DELETE FROM eg_designation where name='Bill Collector';




