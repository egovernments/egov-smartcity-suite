INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'loginForm', NULL, NULL, now(), '/login/securityLogin.jsp', NULL, NULL, NULL,
0, 'User Login', 0, NULL, 'egi');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPERUSER') ,(select id FROM eg_action  WHERE name = 'loginForm'));


INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'LoginForm', NULL, NULL, now(), '/controller/home', NULL, NULL, NULL,
0, 'User Login', 0, NULL, 'egi');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPERUSER') ,(select id FROM eg_action  WHERE name = 'LoginForm'));



INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'CitizenInboxForm', NULL, NULL, now(), '/home', NULL, NULL, NULL,
0, 'User Login', 0, NULL, 'portal');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPERUSER') ,(select id FROM eg_action  WHERE name = 'CitizenInboxForm'));

