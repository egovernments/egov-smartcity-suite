INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'CreateBoundaryTypeForm', NULL, NULL, now(), '/controller/create-boundaryType', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'Jurisdiction'),
0, 'Create Boundary Type New', 1, NULL, 'egi');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPERUSER') ,(select id FROM eg_action  WHERE name = 'CreateBoundaryTypeForm'));


INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'ViewBoundaryTypeForm', NULL, NULL, now(), '/controller/boundaryType/view', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'Jurisdiction'),
0, 'View Boundary Type', 1, NULL, 'egi');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPERUSER') ,(select id FROM eg_action  WHERE name = 'ViewBoundaryTypeForm'));



INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'UpdateBoundaryTypeForm', NULL, NULL, now(), '/controller/boundaryType/update', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'Jurisdiction'),
0, 'Update Boundary Type', 1, NULL, 'egi');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPERUSER') ,(select id FROM eg_action  WHERE name = 'UpdateBoundaryTypeForm'));
