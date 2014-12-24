#UP

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
SEQ_MODULEMASTER.nextval, 'PTIS Administration',  SYSDATE
, 1, 'PTIS_Administration', NULL, (select ID_MODULE from EG_MODULE where MODULE_NAME='Property Tax' ), 'PTIS_Administration', NULL); 



INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ,CONTEXT_ROOT) 
VALUES ( SEQ_EG_ACTION.nextval, 'ChangeStreetRate', NULL, NULL,  sysdate, '/admin/SearchStreetRate.do', NULL, NULL, (select id_module from eg_module where module_name='PTIS Administration'), 1, 'Change Street Rate', 1, NULL,'PTIS');


INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='PROPERTY_TAX_USER'), ( select ID from EG_ACTION where name='ChangeStreetRate' ));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSESSOR'), ( select ID from EG_ACTION where name='ChangeStreetRate'));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSISTANT'), ( select ID from EG_ACTION where name='ChangeStreetRate'));


INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ,CONTEXT_ROOT) 
VALUES ( SEQ_EG_ACTION.nextval, 'beforeChangeStreetRate', NULL, NULL,  sysdate, '/admin/beforeChangeStreetRate.do', NULL, NULL, (select id_module from eg_module where module_name='PTIS Administration'), 1, 'Before Change Street Rate', 0, NULL,'PTIS');


INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='PROPERTY_TAX_USER'), ( select ID from EG_ACTION where name='beforeChangeStreetRate' ));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSESSOR'), ( select ID from EG_ACTION where name='beforeChangeStreetRate'));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSISTANT'), ( select ID from EG_ACTION where name='beforeChangeStreetRate'));


INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ,CONTEXT_ROOT) 
VALUES ( SEQ_EG_ACTION.nextval, 'changeStreetRateSubmitAction', NULL, NULL,  sysdate, '/admin/changeStreetRateSubmit.do', NULL, NULL, (select id_module from eg_module where module_name='PTIS Administration'), 1, 'ChangeStreetRateSubmitAction', 0, NULL,'PTIS');


INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='PROPERTY_TAX_USER'), ( select ID from EG_ACTION where name='changeStreetRateSubmitAction' ));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSESSOR'), ( select ID from EG_ACTION where name='changeStreetRateSubmitAction'));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSISTANT'), ( select ID from EG_ACTION where name='changeStreetRateSubmitAction'));

INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ,CONTEXT_ROOT) 
VALUES ( SEQ_EG_ACTION.nextval, 'getCategoryForCatAmt', NULL, NULL,  sysdate, '/admin/getCategoryForCategoryAmt.jsp', NULL, NULL, (select id_module from eg_module where module_name='PTIS Administration'), 1, 'getCategoryForCategoryAmt', 0, NULL,'PTIS');


INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='PROPERTY_TAX_USER'), ( select ID from EG_ACTION where name='getCategoryForCatAmt' ));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSESSOR'), ( select ID from EG_ACTION where name='getCategoryForCatAmt'));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSISTANT'), ( select ID from EG_ACTION where name='getCategoryForCatAmt'));


INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ,CONTEXT_ROOT) 
VALUES ( SEQ_EG_ACTION.nextval, 'changeStreetRateConfirmAction', NULL, NULL,  sysdate, '/admin/changeStreetRateConfirm.do', NULL, NULL, (select id_module from eg_module where module_name='PTIS Administration'), 1, 'changeStreetRateConfirmAction', 0, NULL,'PTIS');


INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='PROPERTY_TAX_USER'), ( select ID from EG_ACTION where name='changeStreetRateConfirmAction' ));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSESSOR'), ( select ID from EG_ACTION where name='changeStreetRateConfirmAction'));
INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSISTANT'), ( select ID from EG_ACTION where name='changeStreetRateConfirmAction'));


commit;

#DOWN