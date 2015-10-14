--Designation
INSERT INTO egeis_deptdesig(id,designation,department,outsourcedposts,sanctionedposts,version,createddate,lastmodifieddate,createdby,lastmodifiedby)
values(nextval('seq_egeis_deptdesig'),(SELECT id FROM eg_designation where name='Sanitary inspector'),(SELECT id FROM eg_department WHERE name='Health'),0,2,0,now(),now(),1,1);
INSERT INTO egeis_deptdesig(id,designation,department,outsourcedposts,sanctionedposts,version,createddate,lastmodifieddate,createdby,lastmodifiedby)
values(nextval('seq_egeis_deptdesig'),(SELECT id FROM eg_designation where name='Assistant health officer'),(SELECT id FROM eg_department WHERE name='Health'),0,2,0,now(),now(),1,1);


--Position
INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'H-SANITORY INSPECTOR-1',(SELECT id from egeis_deptdesig where department in (SELECT id from eg_department WHERE name='Health') and 
designation in (SELECT id from eg_designation WHERE name='Sanitary inspector')),now(),now(),1,1,false,0);

INSERT INTO eg_position(id,name,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version) values
(nextval('seq_eg_position'),'H-ASSISTANT HEALTH OFFICER-1',(SELECT id from egeis_deptdesig where department in (SELECT id from eg_department WHERE name='Health') and 
designation in (SELECT id from eg_designation WHERE name='Assistant health officer')),now(),now(),1,1,false,0);

--Assignment
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

INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade,lastmodifiedby,lastmodifieddate,createddate,createdby,
fromdate,todate,version,employee,isprimary) VALUES(nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Health officer'),null,(SELECT id FROM eg_department WHERE name='Health'),
(SELECT id FROM eg_position WHERE name='H-HEALTH OFFICER-1'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',1,
(SELECT id FROM egeis_employee WHERE code='E055'),true);


--document types
insert into egtl_document_type  (id, name, applicationtype, mandatory, version) values (nextval('seq_egtl_document_type'), 'photo', 'Create License', 'f', 0);
insert into egtl_document_type  (id, name, applicationtype, mandatory, version) values (nextval('seq_egtl_document_type'), 'document', 'Create License', 'f', 0);

--Workflow Matrix for object type "TradeLicense"
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense','Create License:NEW', NULL, NULL, 'Sanitary inspector', NULL, 'Create License:Sanitary inspector Approved', 'Assistant health officer Approval Pending', 'Assistant health officer', 'Sanitary inspector Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 'Create License:Rejected', NULL, NULL, 'Sanitary inspector', NULL, 'Create License:Sanitary inspector Approved', 'Assistant health officer Approval Pending', 'Assistant health officer', NULL, 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 'Create License:Sanitary inspector Approved', NULL, NULL, 'Assistant health officer', NULL, 'Create License:Assistant health officer Approved', 'Health officer Approval pending', 'Health officer', 'Assistant health officer Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 'Create License:Assistant health officer Approved', NULL, NULL, 'Health officer', NULL,'Create License:END', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, '2015-04-01', '2099-04-01');


--Workflow Matrix for object type "LicenseObjection"
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'LicenseObjection', 'NEW', NULL, NULL, 'Sanitary inspector', NULL, 'Sanitary inspector Approved', 'Assistant health officer Approval Pending', 'Assistant health officer', 'Sanitary inspector Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'LicenseObjection', 'Assistant health officer Approved', NULL, NULL, NULL, NULL,'END', 'END', NULL, NULL, 'Generate License', NULL, NULL, '2015-04-01', '2099-04-01');
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'LicenseObjection', 'Rejected', NULL, NULL, 'Sanitary inspector', NULL, 'Sanitary inspector Approved', 'Assistant health officer Approval Pending', 'Assistant health officer', NULL, 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');


--Workflow Matrix for object type "LicenseTransfer"
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'LicenseTransfer', 'NEW', NULL, NULL, 'Sanitary inspector', NULL, 'Sanitary inspector Approved', 'Assistant health officer Approval Pending', 'Assistant health officer', 'Sanitary inspector Approved', 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'LicenseTransfer', 'Assistant health officer Approved', NULL, NULL, NULL, NULL,'END', 'END', NULL, NULL, 'Generate License', NULL, NULL, '2015-04-01', '2099-04-01');
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'LicenseTransfer', 'Rejected', NULL, NULL, 'Sanitary inspector', NULL, 'Sanitary inspector Approved', 'Assistant health officer Approval Pending', 'Assistant health officer', NULL, 'Forward,Reject', NULL, NULL, '2015-04-01', '2099-04-01');


--Userrole mapping
Insert into eg_userrole values((select id from eg_role  where name  = 'TLCreator'),(select id from eg_user where username ='jayashree'));
Insert into eg_userrole values((select id from eg_role  where name  = 'Employee'),(select id from eg_user where username ='jayashree'));

Insert into eg_userrole values((select id from eg_role  where name  = 'TLApprover'),(select id from eg_user where username ='iffath'));
Insert into eg_userrole values((select id from eg_role  where name  = 'Employee'),(select id from eg_user where username ='iffath'));

Insert into eg_userrole values((select id from eg_role  where name  = 'TLApprover'),(select id from eg_user where username ='mussavir'));
Insert into eg_userrole values((select id from eg_role  where name  = 'Employee'),(select id from eg_user where username ='mussavir'));


--Category Master
Insert into  EGTL_MSTR_CATEGORY (ID,NAME,code,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY) values (nextval('seq_egtl_mstr_category'),'Shops','Shops',Current_date,Current_date,1,1);
Insert into  EGTL_MSTR_CATEGORY (ID,NAME,code,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY) values (nextval('seq_egtl_mstr_category'),'Hotels','Hotels',Current_date,Current_date,1,1);


--SubCategory Master
Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Super Bazar','109',(select id from egtl_mstr_license_type where name='TradeLicense'),(select id from egtl_mstr_business_nature where name='Permanent'),(select id from EGTL_MSTR_CATEGORY where name='Shops') ,null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Small Super Bazar','110',(select id from egtl_mstr_license_type where name='TradeLicense'),(select id from egtl_mstr_business_nature where name='Permanent'),(select id from EGTL_MSTR_CATEGORY where name='Shops'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Mini Super Bazar','111',(select id from egtl_mstr_license_type where name='TradeLicense'),(select id from egtl_mstr_business_nature where name='Permanent'),(select id from EGTL_MSTR_CATEGORY where name='Shops'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Whole Sale','112',(select id from egtl_mstr_license_type where name='TradeLicense'),(select id from egtl_mstr_business_nature where name='Permanent'),(select id from EGTL_MSTR_CATEGORY where name='Shops'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY
(ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Retail','113',(select id from egtl_mstr_license_type where name='TradeLicense'),(select id from egtl_mstr_business_nature where name='Permanent'),(select id from EGTL_MSTR_CATEGORY where name='Shops'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY
(ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'5 Star','16',(select id from egtl_mstr_license_type where name='TradeLicense'),(select id from egtl_mstr_business_nature where name='Permanent'),(select id from EGTL_MSTR_CATEGORY where name='Hotels'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'3 Star','17',(select id from egtl_mstr_license_type where name='TradeLicense'),(select id from egtl_mstr_business_nature where name='Permanent'),(select id from EGTL_MSTR_CATEGORY where name='Hotels'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Lodge','18',(select id from egtl_mstr_license_type where name='TradeLicense'),(select id from egtl_mstr_business_nature where name='Permanent'),(select id from EGTL_MSTR_CATEGORY where name='Hotels'), null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Food Joint','19',(select id from egtl_mstr_license_type where name='TradeLicense'),(select id from egtl_mstr_business_nature where name='Permanent'),(select id from EGTL_MSTR_CATEGORY where name='Hotels'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);
Insert into  EGTL_MSTR_SUB_CATEGORY (ID,NAME,CODE,ID_LICENSE_TYPE,ID_NATURE,ID_CATEGORY,ID_TL_DEPT,ID_SCHEDULE,SECTION_APPLICABLE,PFA_APPLICABLE,FEE_BASED_ON,APPROVAL_REQUIRED,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY,ID_LICENSE_SUB_TYPE,NOC_APPLICABLE) values (nextval('seq_egtl_mstr_sub_category'),'Fast Food Center','115',(select id from egtl_mstr_license_type where name='TradeLicense'),(select id from egtl_mstr_business_nature where name='Permanent'),(select id from EGTL_MSTR_CATEGORY where name='Hotels'),null,null,null,true,null,true,Current_date,Current_date,1,1,null,null);


