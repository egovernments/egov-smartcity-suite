

 Insert into eg_userrole values((select id from eg_role  where name  = 'TLCreator'),(select id from eg_user 
 where username ='roopa'));
Insert into eg_userrole values((select id from eg_role  where name  = 'Employee'),(select id from eg_user where username 
='roopa'));




INSERT INTO egeis_deptdesig(id,designation,department,outsourcedposts,sanctionedposts,
version,createddate,lastmodifieddate,createdby,lastmodifiedby)
values(nextval('seq_egeis_deptdesig'),
(SELECT id FROM eg_designation where name='Revenue Clerk'),
(SELECT id FROM eg_department WHERE name='Health'),0,2,0,now(),now(),1,1);



INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'H-CLERK OFFICER-1',(SELECT id from egeis_deptdesig where department in 
(SELECT id from eg_department WHERE name='Health') and 
designation in (SELECT id from eg_designation WHERE name='Revenue Clerk')),now(),now(),1,1,false,0);

--Assignment
INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment')
,(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Revenue Clerk'),null,(SELECT id FROM eg_department WHERE name='Health'),
(SELECT id FROM eg_position WHERE name='H-CLERK OFFICER-1'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E012'),true);

 