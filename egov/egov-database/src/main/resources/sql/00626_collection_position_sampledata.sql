
INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'R-SECTION MANAGER-1',(SELECT id from egeis_deptdesig where department in(SELECT id from eg_department WHERE name='Revenue') and 
designation in(SELECT id from eg_designation WHERE name='Section manager')),now(),now(),1,1,false,0);


INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Section manager'),null,(SELECT id FROM eg_department WHERE name='Revenue'),
(SELECT id FROM eg_position WHERE name='R-SECTION MANAGER-1'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E062'),true);


Insert into eg_userrole values((select id from eg_role  where name  = 'Remitter'),(select id from eg_user where username ='sumit'));

