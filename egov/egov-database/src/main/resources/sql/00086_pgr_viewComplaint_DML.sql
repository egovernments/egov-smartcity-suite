INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('seq_eg_action'), 'viewComplaintImages', NULL, NULL, now(), '/controller/downloadfile', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_NAME= 'Pgr Masters'),
0, 'view Complaint image', 0, NULL, 'egi');

--rollback delete from eg_action where url='/controller/downloadfile' and name='viewComplaintImages';

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'viewComplaintImages'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where url='/controller/downloadfile' and name='viewComplaintImages' );