DELETE FROM eg_appconfig_values where key_id = (select id from eg_appconfig  where key_name  = 'COLLECTIONDEPARTMENTFORWORKFLOW');
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='COLLECTIONDEPARTMENTFORWORKFLOW'), current_date, 'Revenue,Accounts,Administration',0);

DELETE FROM eg_appconfig_values where key_id = (select id from eg_appconfig  where key_name  = 'COLLECTIONDESIGNATIONFORCSCOPERATORASCLERK');
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='COLLECTIONDESIGNATIONFORCSCOPERATORASCLERK'), current_date, 'Senior Assistant,Junior Assistant',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'COLLECTIONDEPARTMENTFORWORKFLOWAPPROVER', 'Department for Workflow Approver',0, (select id from eg_module where name='Collection'));
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='COLLECTIONDEPARTMENTFORWORKFLOWAPPROVER' AND MODULE =(select id from eg_module where name='Collection')),current_date, 'Administration',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'COLLECTIONDESIGNATIONFORAPPROVER','Designation for Collection workflow Approver',0, (select id from eg_module where name='Collection')); 
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='COLLECTIONDESIGNATIONFORAPPROVER'),current_date, 'Manager',0);

