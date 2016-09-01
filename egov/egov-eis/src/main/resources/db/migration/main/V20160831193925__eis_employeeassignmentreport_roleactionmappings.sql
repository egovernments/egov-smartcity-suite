-----Employee assignments roleactions-----
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'), 'EIS Reports', true, 'eis', (select id from eg_module where name='EIS' and parentmodule is null), 'Reports', 2);

INSERT into eg_role values(nextval('seq_eg_role'),'EIS Report Viewer','EIS Report Viewer',current_date,1,1,current_date,0);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'EmployeeAssignmentSearchForm','/reports/employeeassignments/searchform',null,
(select id from eg_module where name='EIS Reports'),1,'Employee Assignment Report','true','eis',0,1,now(),1,now(),(select id from eg_module  where name = 'EIS'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'AjaxEmployeeCodes','/reports/searchemployeecodes',null,
(select id from eg_module where name='EIS Reports'),1,'Ajax Employee Codes','false','eis',0,1,now(),1,now(),(select id from eg_module  where name = 'EIS'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'AjaxEmployeeDesignations','/reports/searchdesignations',null,
(select id from eg_module where name='EIS Reports'),1,'Ajax Employee Designations','false','eis',0,1,now(),1,now(),(select id from eg_module  where name = 'EIS'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'AjaxEmployeePositions','/reports/searchpositions',null,
(select id from eg_module where name='EIS Reports'),1,'Ajax Employee Positions','false','eis',0,1,now(),1,now(),(select id from eg_module  where name = 'EIS'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'Search Employee Assignments','/reports/employeeassignments/search',null,
(select id from eg_module where name='EIS Reports'),1,'Search Employee Assignments','false','eis',0,1,now(),1,now(),(select id from eg_module  where name = 'EIS'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'Employee Assignment pdf','/reports/employeeassignments/pdf',null,
(select id from eg_module where name='EIS Reports'),1,'Search Employee Assignments pdf','false','eis',0,1,now(),1,now(),(select id from eg_module  where name = 'EIS'));

insert into eg_roleaction values((select id from eg_role where name='Employee Admin'),(select id from eg_action where name='EmployeeAssignmentSearchForm' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'ERP Report Viewer'),(select id from eg_action where name ='EmployeeAssignmentSearchForm' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='EmployeeAssignmentSearchForm' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'EIS Report Viewer'),(select id from eg_action where name ='EmployeeAssignmentSearchForm' and contextroot = 'eis'));

insert into eg_roleaction values((select id from eg_role where name='Employee Admin'),(select id from eg_action where name='AjaxEmployeeCodes' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'ERP Report Viewer'),(select id from eg_action where name ='AjaxEmployeeCodes' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxEmployeeCodes' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'EIS Report Viewer'),(select id from eg_action where name ='AjaxEmployeeCodes' and contextroot = 'eis'));

insert into eg_roleaction values((select id from eg_role where name='Employee Admin'),(select id from eg_action where name='AjaxEmployeeDesignations' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'ERP Report Viewer'),(select id from eg_action where name ='AjaxEmployeeDesignations' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxEmployeeDesignations' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'EIS Report Viewer'),(select id from eg_action where name ='AjaxEmployeeDesignations' and contextroot = 'eis'));

insert into eg_roleaction values((select id from eg_role where name='Employee Admin'),(select id from eg_action where name='AjaxEmployeePositions' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'ERP Report Viewer'),(select id from eg_action where name ='AjaxEmployeePositions' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxEmployeePositions' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'EIS Report Viewer'),(select id from eg_action where name ='AjaxEmployeePositions' and contextroot = 'eis'));

insert into eg_roleaction values((select id from eg_role where name='Employee Admin'),(select id from eg_action where name='Search Employee Assignments' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'ERP Report Viewer'),(select id from eg_action where name ='Search Employee Assignments' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Search Employee Assignments' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'EIS Report Viewer'),(select id from eg_action where name ='Search Employee Assignments' and contextroot = 'eis'));

insert into eg_roleaction values((select id from eg_role where name='Employee Admin'),(select id from eg_action where name='Employee Assignment pdf' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'ERP Report Viewer'),(select id from eg_action where name ='Employee Assignment pdf' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Employee Assignment pdf' and contextroot = 'eis'));
insert into eg_roleaction values((select id from eg_role where name = 'EIS Report Viewer'),(select id from eg_action where name ='Employee Assignment pdf' and contextroot = 'eis'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name in('EmployeeAssignmentSearchForm','AjaxEmployeeCodes','AjaxEmployeeDesignations','AjaxEmployeePositions','Search Employee Assignments','Employee Assignment pdf')) and roleid in(select id from eg_role where name in('Employee Admin','ERP Report Viewer','Super User','EIS Report Viewer'));
--rollback delete from eg_action where name in('EmployeeAssignmentSearchForm','AjaxEmployeeCodes','AjaxEmployeeDesignations','AjaxEmployeePositions','Search Employee Assignments','Employee Assignment pdf') and contextroot='eis';
--rollback delete from eg_role where name='EIS Report Viewer';
--rollback delete from eg_module where name='EIS Reports';

---Employee Assignment Report feature mapping--

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Employee Assignment Report','Employee Assignment Report',(select id from eg_module  where name = 'EIS'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'EmployeeAssignmentSearchForm') ,(select id FROM eg_feature WHERE name = 'Employee Assignment Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxEmployeeCodes') ,(select id FROM eg_feature WHERE name = 'Employee Assignment Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxEmployeeDesignations') ,(select id FROM eg_feature WHERE name = 'Employee Assignment Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxEmployeePositions') ,(select id FROM eg_feature WHERE name = 'Employee Assignment Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search Employee Assignments') ,(select id FROM eg_feature WHERE name = 'Employee Assignment Report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Employee Assignment pdf') ,(select id FROM eg_feature WHERE name = 'Employee Assignment Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Employee Assignment Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Employee Admin') ,(select id FROM eg_feature WHERE name = 'Employee Assignment Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Employee Assignment Report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'EIS Report Viewer') ,(select id FROM eg_feature WHERE name = 'Employee Assignment Report'));