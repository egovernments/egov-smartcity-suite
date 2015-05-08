INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT) Values (nextval('SEQ_EG_ACTION'), 'RefreshCitizenInbox', NULL, NULL, now(), '/home/refreshInbox', NULL, NULL, NULL, 0, 'Refresh Citizen Inboxe', 0, NULL, 'portal');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Citizen') ,(select id FROM eg_action  WHERE name = 'RefreshCitizenInbox'));
