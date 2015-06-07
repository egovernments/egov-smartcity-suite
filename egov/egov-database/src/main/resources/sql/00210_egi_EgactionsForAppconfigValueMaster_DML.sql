


INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'CreateAppConfig', NULL, NULL, now(),
 '/appConfig/create', NULL, NULL, (SELECT id FROM EG_MODULE WHERE name = 'Configuration'),
0, 'Create AppConfig', 'true', NULL, 'egi');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'CreateAppConfig'));




INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'AppConfigModuleAutocomplete', NULL, NULL, now(),
 '/appConfig/modules', NULL, NULL, (SELECT id FROM EG_MODULE WHERE name = 'Configuration'),
0, 'AppConfigModuleAutocomplete','false', NULL, 'egi');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values 
((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AppConfigModuleAutocomplete'));





INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'modifyAppConfig', NULL, NULL, now(),
 '/appConfig/update', NULL, NULL, (SELECT id FROM EG_MODULE WHERE name = 'Configuration'),
1, 'Modify AppConfig', 'true', NULL, 'egi');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') 
,(select id FROM eg_action  WHERE name = 'modifyAppConfig'));


