#UP

INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ,CONTEXT_ROOT) 
VALUES ( SEQ_EG_ACTION.nextval, 'citizenDCB', NULL, NULL,  sysdate, '/cocpt/currentDCB.action', NULL, NULL, (select id_module from eg_module where module_name='PTIS Collection'), 0, 'citizenDCB', 0, NULL,'PTIS');
 
#DOWN

DELETE from EG_ACTION where name = 'citizenDCB';
