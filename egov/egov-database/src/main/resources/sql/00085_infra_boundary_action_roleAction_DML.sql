INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'CreateBoundaryForm', NULL, NULL, now(), '/controller/create-boundary', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'Jurisdiction'),
0, 'CreateBoundaryForm', 0, NULL, 'egi');

INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'ViewBoundary', NULL, NULL, now(), '/controller/view-boundary', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'Jurisdiction'),
0, 'ViewBoundary', 0, NULL, 'egi');

INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'UpdateBoundary', NULL, NULL, now(), '/controller/update-boundary', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'Jurisdiction'),
0, 'UpdateBoundary', 0, NULL, 'egi');


INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) LIKE 'SUPERUSER') ,(select id FROM eg_action  WHERE name = 'CreateBoundaryForm'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) LIKE 'SUPERUSER') ,(select id FROM eg_action  WHERE name = 'ViewBoundary'));

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) LIKE 'SUPERUSER') ,(select id FROM eg_action  WHERE name = 'UpdateBoundary'));


