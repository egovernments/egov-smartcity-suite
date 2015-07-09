
INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('seq_eg_action'), 'UpdateComplaintType', NULL, NULL, now(), '/complaint-type/update', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_NAME= 'Pgr Masters'),
0, 'Update Complaint Type', 1, NULL, 'pgr');

--rollback delete from eg_action where url='/complaint-type/update' and name='UpdateComplaintType';

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'UpdateComplaintType'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where url='/complaint-type/update' and name='UpdateComplaintType' );

INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('seq_eg_action'), 'ViewComplaintType', NULL, NULL, now(), '/view-complaintType/form', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_NAME= 'Pgr Masters'),
0, 'View Complaint Type', 1, NULL, 'pgr');

--rollback delete from eg_action where url='/view-complaintType/form';

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'ViewComplaintType'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where url='/view-complaintType/form');
