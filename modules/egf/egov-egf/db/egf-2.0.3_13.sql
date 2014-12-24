UPDATE eg_action SET queryparams='submitType=getTdsAndotherdtls\&mode=create' WHERE name='Works Bill - Create';

INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,
MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ) VALUES ( 
seq_eg_action.NEXTVAL, 'Works Bill - Modify', NULL, NULL,  TO_DATE( '01/09/2009 04:26:12 PM', 'MM/DD/YYYY HH:MI:SS AM')
, '/billsaccounting/worksBill.do', 'submitType=getTdsAndotherdtls\&mode=modify', NULL, 25, 1, 'Modify Works Bill'
, 0, '/HelpAssistance/AP/WorksBill-Modify_AP.htm'); 

INSERT INTO eg_roleaction_map (roleid,actionid) VALUES((SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME IN ('SuperUser','Super User','SUPER USER')),(SELECT id FROM eg_action WHERE name='Works Bill - Modify'));
COMMIT;
