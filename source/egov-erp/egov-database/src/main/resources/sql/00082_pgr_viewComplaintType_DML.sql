
INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('seq_eg_action'), 'viewComplaintTypeResult', NULL, NULL, now(), '/view-complaintType/result', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_NAME= 'Pgr Masters'),
0, 'view Complaint Type result', 0, NULL, 'pgr');

--rollback delete from eg_action where url='/view-complaintType/result' and name='viewComplaintTypeResult';

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPERUSER') ,(select id FROM eg_action  WHERE name = 'viewComplaintTypeResult'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where url='/view-complaintType/result' and name='viewComplaintTypeResult' );

