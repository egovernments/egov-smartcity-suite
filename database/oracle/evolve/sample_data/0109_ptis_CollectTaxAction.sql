#UP

INSERT INTO EG_MODULE ( ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL,
BASEURL, PARENTID, MODULE_DESC, ORDER_NUM ) VALUES ( 
SEQ_MODULEMASTER.nextval, 'PTIS Collection',  SYSDATE
, 1, 'PTIS Collection', NULL, (select ID_MODULE from EG_MODULE where MODULE_NAME='Property Tax' ), 'PTIS Collection', NULL); 



INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ,CONTEXT_ROOT) 
VALUES ( SEQ_EG_ACTION.nextval, 'viewCollectTax', NULL, NULL,  sysdate, '/property/viewCollectTax', NULL, NULL, (select id_module from eg_module where module_name='PTIS Collection'), 0, 'Change Street Rate', 0, NULL,'PTIS');


INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='SuperUser'), ( select ID from EG_ACTION where name='viewCollectTax' ));





#DOWN
