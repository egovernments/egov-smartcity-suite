INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'ViewDepartmentForm', NULL, NULL, now(), '/department/view', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'Department'),
0, 'View Department', 1, NULL, 'egi');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ViewDepartmentForm'));

INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'UpdateDepartmentForm', NULL, NULL, now(), '/department/update', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'Department'),
0, 'Update Department', 1, NULL, 'egi');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'UpdateDepartmentForm'));

update eg_action set url = '/department/create' where url = '/create-department';

--rollback DELETE FROM EG_ROLEACTION_MAP WHERE ROLEID = (select id from eg_role where UPPER(name) LIKE 'SUPER USER') AND ACTIONID = (select id FROM eg_action  WHERE name = 'UpdateDepartmentForm');
--rollback DELETE FROM EG_ACTION WHERE NAME = 'UpdateDepartmentForm';
--rollback DELETE FROM EG_ROLEACTION_MAP WHERE ROLEID = (select id from eg_role where UPPER(name) LIKE 'SUPER USER') AND ACTIONID = (select id FROM eg_action  WHERE name = 'ViewDepartmentForm');
--rollback DELETE FROM EG_ACTION WHERE NAME = 'ViewDepartmentForm';
--rollback update eg_action set url = '/create-department' where url = '/department/create';