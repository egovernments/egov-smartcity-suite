INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'PROPERTYTAXDEPARTMENTFORWORKFLOW', 
'Department for Workflow',0, (select id from eg_module where name='Property Tax')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='PROPERTYTAXDEPARTMENTFORWORKFLOW' AND MODULE =(select id from eg_module where name='Property Tax')),current_date, 'Revenue',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'PROPERTYTAXROLEFORNONEMPLOYEE', 
'roles for Property tax workflow',0, (select id from eg_module where name='Property Tax')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='PROPERTYTAXROLEFORNONEMPLOYEE'),current_date, 'CSC Operator',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'),'PROPERTYTAXDESIGNATIONFORWORKFLOW', 
'Designation for Workflow',0, (select id from eg_module where name='Property Tax')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='PROPERTYTAXDESIGNATIONFORWORKFLOW' and MODULE =(select id from eg_module where name='Property Tax')),current_date, 'Revenue Clerk',0);

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Create Property' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='CSC Operator' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Populate Categories by Property Type' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='CSC Operator' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Load Admin Boundaries' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='CSC Operator' ));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='Create Property Submit' and contextroot='ptis'),
(SELECT id FROM eg_role  WHERE name='CSC Operator' ));

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'PropertyImpl', 'Created', NULL, NULL, 'NULL','NEW ASSESSMENT', 'Create' || ':' || 'NEW', 'Revenue Clerk approval pending', 'Revenue Clerk', 'Revenue Clerk Approved', 'Forward', NULL, NULL, '2015-04-01', '2099-04-01');