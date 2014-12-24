#UP

--modify property work flow
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSISTANT'), ( select ID from EG_ACTION where name='Modify Property'  AND CONTEXT_ROOT = 'PTIS' ));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSISTANT'), ( select ID from EG_ACTION where name='Modify Property Confirm'  AND CONTEXT_ROOT = 'PTIS' ));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSISTANT'), ( select ID from EG_ACTION where name='Modify Property Submit'  AND CONTEXT_ROOT = 'PTIS' ));

INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSESSOR'), ( select ID from EG_ACTION where name='Modify Property'  AND CONTEXT_ROOT = 'PTIS' ));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSESSOR'), ( select ID from EG_ACTION where name='Modify Property Confirm'  AND CONTEXT_ROOT = 'PTIS' ));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSESSOR'), ( select ID from EG_ACTION where name='Modify Property Submit'  AND CONTEXT_ROOT = 'PTIS' ));

Insert into EG_WF_TYPES
   (ID_TYPE, MODULE_ID, WF_TYPE, WF_LINK, CREATED_BY, RENDER_YN, GROUP_YN, FULL_QUALIFIED_NAME, DISPLAY_NAME)
 Values
   (EG_WF_TYPES_SEQ.nextval, (select ID_MODULE from EG_MODULE where MODULE_NAME='Property Tax'), 'WorkFlowModifyProperty', '/ptis/property/modifyPropertyWorkFlow.do?submitType=beforeApproval&ViewType=WorkFlowObject&WorkFlowModifyId=:ID', (select ID_USER from eg_user where USER_NAME like 'egovernments'), 'Y', 'N', 'org.egov.ptis.property.model.WorkFlowModifyProperty', 'Modify Property');
   
#DOWN