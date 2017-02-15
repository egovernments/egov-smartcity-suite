----------------------------- app config value to identify Non-Employee for adtax-----------------

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'ADTAXROLEFORNONEMPLOYEE', 'roles for advertisement tax workflow',0, (select id from eg_module where name='Advertisement Tax')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ADTAXROLEFORNONEMPLOYEE'),current_date, 'CSC Operator',0);

--------------------------------workflow matrix for CSC Operator----------------------

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'AdvertisementPermitDetail', 'Third Party operator created', NULL, NULL, NULL, 'CREATEADVERTISEMENT', 'NEW', 'Junior/Senior Assistance approval pending', 'Junior Assistant,Senior Assistant', 'CREATED', 'Forward', NULL, NULL, '2017-01-01', '2099-04-01');

------------------------------appconfig value for to retrieve CSC operator designation --------------------------

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'),'ADTAXDESIGNATIONFORCSCOPERATORWORKFLOW', 'Designation for Csc Workflow',0, (select id from eg_module where name='Advertisement Tax'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ADTAXDESIGNATIONFORCSCOPERATORWORKFLOW'), current_date, 'Junior Assistant,Senior Assistant',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'ADTAXDEPARTMENTFORCSCOPERATORWORKFLOW', 'Department for Csc Workflow',0, (select id from eg_module where name='Advertisement Tax'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ADTAXDEPARTMENTFORCSCOPERATORWORKFLOW'), current_date, 'Town Planning,Revenue',0);


INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'ADTAXDEPARTMENTFORWORKFLOW', 'Department for city level Workflow',0, (select id from eg_module where name='Advertisement Tax')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ADTAXDEPARTMENTFORWORKFLOW'), current_date, 'Town Planning,Revenue,Accounts,Administration',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'),'ADTAXDESIGNATIONFORWORKFLOW', 'Designation for city level Workflow',0, (select id from eg_module where name='Advertisement Tax')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ADTAXDESIGNATIONFORWORKFLOW'), current_date, 'Junior Assistant,Senior Assistant',0);


----------------------------create advertisement roleaction for CSC operator------------------

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name ='CSC Operator') ,(select id FROM eg_action  WHERE name = 'CreateHoarding'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name ='CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxSubCategories'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name ='CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxGetPropertyassessmentDetails'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name ='CSC Operator') ,(select id FROM eg_action  WHERE name = 'Load Block By Ward'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name ='CSC Operator') ,(select id FROM eg_action  WHERE name = 'calculateTaxAmount'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name ='CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxDesignationsByDepartment'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name ='CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxApproverByDesignationAndDepartment'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name ='CSC Operator') ,(select id FROM eg_action  WHERE name = 'HoardingSuccess'));


---------------------advertisement acknowledgement role action mapping------------------------


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Hoarding Success Acknowledgement','/hoarding/showack/',(select id from eg_module where name='AdvertisementTaxTransactions'),9,'Hoarding Success Acknowledgement',false,'adtax',(select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'Hoarding Success Acknowledgement'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='CSC Operator') ,(select id FROM eg_action  WHERE name = 'Hoarding Success Acknowledgement'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Print Hoarding Success Acknowledgement','/hoarding/printack',(select id from eg_module where name='AdvertisementTaxTransactions'),9,'Print Hoarding Success Acknowledgement',false,'adtax',(select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'Print Hoarding Success Acknowledgement'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='CSC Operator') ,(select id FROM eg_action  WHERE name = 'Print Hoarding Success Acknowledgement'));

---------------------advertisement acknowledgement feature action mapping------------------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Hoarding Success Acknowledgement') ,(select id FROM eg_feature WHERE name = 'Create Advertisement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Print Hoarding Success Acknowledgement') ,(select id FROM eg_feature WHERE name = 'Create Advertisement'));


INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'CSC Operator') ,(select id FROM eg_feature WHERE name ='Create Advertisement'));
