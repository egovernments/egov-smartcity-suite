#UP

Insert into EG_ACTION
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, 
    URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, 
    DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
 Values
   (SEQ_EG_ACTION.nextval, 'WorkflowPropertyCreate', NULL, NULL, SYSDATE, 
    '/cocpt/beforeWFCreateProperty.action', NULL, NULL, (select id_module from eg_module where module_name='New Property'), 1, 
    'Form6 Create New Property WorkFlow', 1, NULL, 'PTIS');
    
    INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME LIKE 'ASSISTANT'), (SELECT id FROM EG_ACTION WHERE name='WorkflowPropertyCreate' AND CONTEXT_ROOT = 'PTIS' ));

#DOWN

