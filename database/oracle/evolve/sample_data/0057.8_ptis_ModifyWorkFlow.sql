#UP
--modify property work flow
delete from EG_ROLEACTION_MAP where actionid in (SELECT id FROM EG_ACTION WHERE name='Notice7' AND CONTEXT_ROOT = 'PTIS') and roleid in (select id_role from eg_roles where role_name in ('ASSESSOR','PROPERTY_TAX_USER'));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME = 'ASSISTANT'), (SELECT id FROM EG_ACTION WHERE name='Notice7' AND CONTEXT_ROOT = 'PTIS' ));


update eg_wf_types set DISPLAY_NAME = 'Name Transfer' where WF_TYPE = 'PropWorkFlowMutation';

#DOWN