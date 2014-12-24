#UP

Insert into EG_WF_TYPES
   (ID_TYPE, MODULE_ID, WF_TYPE, WF_LINK, CREATED_BY, 
    CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, RENDER_YN, GROUP_YN, 
    FULL_QUALIFIED_NAME, DISPLAY_NAME)
 Values
    (EG_WF_TYPES_SEQ.nextval, (select ID_MODULE from EG_MODULE where MODULE_NAME='Property Tax'), 'WorkflowProperty', '/ptis/coc/beforeWFCreateProperty!handleWorkFlow.action?ViewType=WorkFlowObject&WorkFlowTrnsfrId=:ID', 1, 
    NULL, NULL, NULL, 'Y', 'N', 
    'org.egov.ptis.property.model.WorkflowProperty', 'CreateProperty');




Insert into EG_ACTION
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, 
    URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, 
    DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
 Values
   (SEQ_EG_ACTION.nextval, 'WorkflowPropertyCreate', NULL, NULL, SYSDATE, 
    '/coc/beforeWFCreateProperty.action', NULL, NULL, (select id_module from eg_module where module_name='New Property'), 1, 
    'Form6 Create New Property WorkFlow', 1, NULL, 'PTIS');
    
    INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME LIKE 'ASSISTANT'), (SELECT id FROM EG_ACTION WHERE name='WorkflowPropertyCreate' AND CONTEXT_ROOT = 'PTIS' ));

#DOWN
