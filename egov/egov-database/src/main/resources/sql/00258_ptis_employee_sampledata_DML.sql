--DESIGNATION
INSERT INTO eg_designation(id,name,description,chartofaccounts,version,createddate,lastmodifieddate,createdby,lastmodifiedby) VALUES
(nextval('seq_eg_designation'),'Revenue officer','Revenue officer',null,0,now(),now(),1,1);

--EGEIS_DEPTDESIG
INSERT INTO egeis_deptdesig(id,designation,department,outsourcedposts,sanctionedposts,version,createddate,lastmodifieddate,createdby,lastmodifiedby)
values(nextval('seq_egeis_deptdesig'),(SELECT id FROM eg_designation where name='Assistant'),(SELECT id FROM eg_department WHERE name='Revenue'),0,2,0,now(),now(),1,1);

INSERT INTO egeis_deptdesig(id,designation,department,outsourcedposts,sanctionedposts,version,createddate,lastmodifieddate,createdby,lastmodifiedby)
values(nextval('seq_egeis_deptdesig'),(SELECT id FROM eg_designation where name='Revenue officer'),(SELECT id FROM eg_department WHERE name='Revenue'),0,2,0,now(),now(),1,1);

INSERT INTO egeis_deptdesig(id,designation,department,outsourcedposts,sanctionedposts,version,createddate,lastmodifieddate,createdby,lastmodifiedby)
values(nextval('seq_egeis_deptdesig'),(SELECT id FROM eg_designation where name='Commissioner'),(SELECT id FROM eg_department WHERE name='Revenue'),0,2,0,now(),now(),1,1);

--EG_POSITION
INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-ASSISTANT-1',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Assistant')),now(),now(),1,1,false,0);

INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-ASSISTANT-2',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Assistant')),now(),now(),1,1,false,0);

INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-ASSISTANT-3',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Assistant')),now(),now(),1,1,false,0);

INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-ASSISTANT-4',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Assistant')),now(),now(),1,1,false,0);

INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-ASSISTANT-5',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Assistant')),now(),now(),1,1,false,0);

INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-REVENUE OFFICER-1',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Revenue officer')),now(),now(),1,1,false,0);

INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-REVENUE OFFICER-2',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Revenue officer')),now(),now(),1,1,false,0);

INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-COMMISIONER-2',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Commissioner')),now(),now(),1,1,false,0);

--EGEIS_ASSIGNMENT
INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Assistant'),null,(SELECT id FROM eg_department WHERE name='Revenue'),
(SELECT id FROM eg_position WHERE name='R-ASSISTANT-1'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E013'),true);

INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Assistant'),null,(SELECT id FROM eg_department WHERE name='Revenue'),
(SELECT id FROM eg_position WHERE name='R-ASSISTANT-2'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E014'),true);

INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Assistant'),null,(SELECT id FROM eg_department WHERE name='Revenue'),
(SELECT id FROM eg_position WHERE name='R-ASSISTANT-3'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E002'),true);

INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Assistant'),null,(SELECT id FROM eg_department WHERE name='Revenue'),
(SELECT id FROM eg_position WHERE name='R-ASSISTANT-4'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E005'),true);

INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Assistant'),null,(SELECT id FROM eg_department WHERE name='Revenue'),
(SELECT id FROM eg_position WHERE name='R-ASSISTANT-5'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E009'),true);


INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Revenue officer'),null,(SELECT id FROM eg_department WHERE name='Revenue'),
(SELECT id FROM eg_position WHERE name='R-REVENUE OFFICER-1'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E023'),true);

INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Revenue officer'),null,(SELECT id FROM eg_department WHERE name='Revenue'),
(SELECT id FROM eg_position WHERE name='R-REVENUE OFFICER-2'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E024'),true);


INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Commissioner'),null,(SELECT id FROM eg_department WHERE name='Revenue'),
(SELECT id FROM eg_position WHERE name='R-COMMISIONER-2'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E028'),true);


--rollback DELETE FROM egeis_assignment WHERE employee in (SELECT id FROM egeis_employee   where code in ('E013','E014','E002','E005','E009','E023','E024','E028'));
--rollback DELETE FROM eg_position WHERE name in ('R-ASSISTANT-1','R-ASSISTANT-2','R-ASSISTANT-3','R-ASSISTANT-4','R-ASSISTANT-5','R-REVENUE OFFICER-1','R-REVENUE OFFICER-2','R-COMMISIONER-2');
--rollback DELETE FROM egeis_deptdesig where designation in (SELECT id FROM eg_designation where name='Assistant') and department in (SELECT id FROM eg_department WHERE name='Revenue');
--rollback DELETE FROM egeis_deptdesig where designation in (SELECT id FROM eg_designation where name='Revenue officer') and department in (SELECT id FROM eg_department WHERE name='Revenue');
--rollback DELETE FROM egeis_deptdesig where designation in (SELECT id FROM eg_designation where name='Commissioner') and department in (SELECT id FROM eg_department WHERE name='Revenue');
--rollback DELETE FROM eg_designation where name='Revenue officer';



