INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'CreateRoleForm', NULL, NULL, now(), '/controller/create-role', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'User Management'),
0, 'Create Role New', 1, NULL, 'egi');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'CreateRoleForm'));



INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'ViewRoleForm', NULL, NULL, now(), '/controller/view-role', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'User Management'),
0, 'View Role', 1, NULL, 'egi');



INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ViewRoleForm'));


INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'UpdateRoleForm', NULL, NULL, now(), '/controller/update-role', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'User Management'),
0, 'Update Role', 1, NULL, 'egi');



INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'UpdateRoleForm'));
