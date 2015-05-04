INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT) Values (nextval('SEQ_EG_ACTION'), 'ViewuserRoleForm', NULL, NULL, now(), '/userrole/viewsearch', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'User Management'),0, 'Search User Role', 1, NULL, 'egi');

INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT) Values (nextval('SEQ_EG_ACTION'), 'UpdateuserRoleForm', NULL, NULL, now(), '/userrole/update', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'User Management'), 0, 'Update User Role', 0, NULL, 'egi');

INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT) Values (nextval('SEQ_EG_ACTION'), 'UpdateuserRole', NULL, NULL, now(), '/userrole/update/updateUserRole', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'User Management'), 0, 'Update User Roles ', 0, NULL, 'egi');

INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT) Values (nextval('SEQ_EG_ACTION'), 'viewuserRole', NULL, NULL, now(), '/userrole/view', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'User Management'), 0, 'View User Roles ', 0, NULL, 'egi');

INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT) Values (nextval('SEQ_EG_ACTION'), 'AjaxLoadRoleByUser', NULL, NULL, now(), '/userRole/ajax/rolelist-for-user', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'User Management'),0, 'AjaxLoadRoleByUser', 0, NULL, 'egi');

INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT) Values (nextval('SEQ_EG_ACTION'), 'searchUserRoleForm', NULL, NULL, now(), '/userrole/search', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'User Management'), 0, 'Search User Role', 0, NULL, 'egi');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID)  (
select (select id from eg_role where UPPER(name) LIKE 'SUPERUSER') as roleid, id as actionid from eg_action where name in ('searchUserRoleForm',
'ViewuserRoleForm','UpdateuserRoleForm','AjaxLoadRoleByUser','UpdateuserRole','viewuserRole'));


--rollback delete from eg_roleaction_map where actionid in ( select id from eg_action where name in ('searchUserRoleForm','ViewuserRoleForm','UpdateuserRoleForm','AjaxLoadRoleByUser','UpdateuserRole','ViewuserRole'));
--rollback delete from EG_ACTION where id in ( select id from eg_action where name in ('searchUserRoleForm','ViewuserRoleForm','UpdateuserRoleForm','AjaxLoadRoleByUser','UpdateuserRole','ViewuserRole'));