INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'budgetcheckurl','budgetcheckurl',0, (select id from eg_module where name='Council Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='budgetcheckurl' AND  MODULE =(select id from eg_module where name='Council Management')),current_date,  '/EGF/report/budgetVarianceReport.action',0);


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Budget Variance Report') ,(select id FROM eg_feature WHERE name = 'Create Preamble'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Budget Variance Report') ,(select id FROM eg_feature WHERE name = 'Update Preamble'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Budget Variance Report') ,(select id FROM eg_feature WHERE name = 'View Preamble'));


INSERT into EG_ROLEACTION(ROLEID,ACTIONID) VALUES ((select id from eg_role where name='Council Clerk'),(select id from eg_action where name ='Budget Variance Report'));
INSERT into EG_ROLEACTION(ROLEID,ACTIONID) VALUES ((select id from eg_role where name='Council Management Approver'),(select id from eg_action where name ='Budget Variance Report'));
INSERT into EG_ROLEACTION(ROLEID,ACTIONID) VALUES ((select id from eg_role where name='Council Management Creator'),(select id from eg_action where name ='Budget Variance Report'));

