INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'License','NEW', NULL, NULL, 'Sanitary inspector', NULL, 'Sanitary inspector Approved', 'Assistant health officer Approval Pending', 'Assistant health officer', 'Sanitary inspector Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'License', 'Assistant health officer Approved', NULL, NULL, NULL, NULL,'END', 'END', NULL, NULL, 'Generate License', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'License', 'Rejected', NULL, NULL, 'Sanitary inspector', NULL, 'Sanitary inspector Approved', 'Assistant health officer Approval Pending', 'Assistant health officer', NULL, 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');


Insert into EG_ROLE (ID,NAME,DESCRIPTION,CREATEDDATE,CREATEDBY,LASTMODIFIEDBY,LASTMODIFIEDDATE,VERSION) values (NEXTVAL('SEQ_EG_ROLE'),'TLCreator','TradeLicense creator',NOW(),1,1,NOW(),0);

Insert into EG_ROLE (ID,NAME,DESCRIPTION,CREATEDDATE,CREATEDBY,LASTMODIFIEDBY,LASTMODIFIEDDATE,VERSION) values (NEXTVAL('SEQ_EG_ROLE'),'TLApprover','TradeLicense Approver',NOW(),1,1,NOW(),0);


INSERT INTO egeis_deptdesig(id,designation,department,outsourcedposts,sanctionedposts,version,createddate,lastmodifieddate,createdby,lastmodifiedby)
values(nextval('seq_egeis_deptdesig'),(SELECT id FROM eg_designation where name='Sanitary inspector'),(SELECT id FROM eg_department WHERE name='Health'),0,2,0,now(),now(),1,1);

INSERT INTO egeis_deptdesig(id,designation,department,outsourcedposts,sanctionedposts,version,createddate,lastmodifieddate,createdby,lastmodifiedby)
values(nextval('seq_egeis_deptdesig'),(SELECT id FROM eg_designation where name='Assistant health officer'),(SELECT id FROM eg_department WHERE name='Health'),0,2,0,now(),now(),1,1);


INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'H-SANITORY INSPECTOR-1',(SELECT id from egeis_deptdesig where department in (SELECT id from eg_department WHERE name='Health') and 
designation in (SELECT id from eg_designation WHERE name='Sanitary inspector')),now(),now(),1,1,false,0);

INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'H-ASSISTANT HEALTH OFFICER-1',(SELECT id from egeis_deptdesig where department in (SELECT id from eg_department WHERE name='Health') and 
designation in (SELECT id from eg_designation WHERE name='Assistant health officer')),now(),now(),1,1,false,0);


INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Sanitary inspector'),null,(SELECT id FROM eg_department WHERE name='Health'),
(SELECT id FROM eg_position WHERE name='H-SANITORY INSPECTOR-1'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E001'),true);

INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Assistant health officer'),null,(SELECT id FROM eg_department WHERE name='Health'),
(SELECT id FROM eg_position WHERE name='H-ASSISTANT HEALTH OFFICER-1'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E018'),true);


Insert into eg_userrole values((select id from eg_role  where name  = 'TLCreator'),(select id from eg_user where username ='jayashree'));
Insert into eg_userrole values((select id from eg_role  where name  = 'Employee'),(select id from eg_user where username ='jayashree'));

Insert into eg_userrole values((select id from eg_role  where name  = 'TLApprover'),(select id from eg_user where username ='iffath'));
Insert into eg_userrole values((select id from eg_role  where name  = 'Employee'),(select id from eg_user where username ='iffath'));

