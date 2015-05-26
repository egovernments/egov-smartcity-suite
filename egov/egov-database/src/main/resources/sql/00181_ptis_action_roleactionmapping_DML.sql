Insert into eg_action (ID,NAME,ENTITYID,TASKID,UPDATEDTIME,URL,QUERYPARAMS,URLORDERID,MODULE_ID,ORDER_NUMBER,DISPLAY_NAME,
IS_ENABLED,ACTION_HELP_URL,CONTEXT_ROOT) values (nextval('SEQ_EG_ACTION'),'Populate LocationFactors',
null,null,current_timestamp,'/common/ajaxCommon-locationFactorsByWard.action',null,null,(select id_module from eg_module where 
module_name='Existing property'),0,'Populate LocationFactors',0,null,'ptis');

Insert into eg_action (ID,NAME,ENTITYID,TASKID,UPDATEDTIME,URL,QUERYPARAMS,URLORDERID,MODULE_ID,ORDER_NUMBER,DISPLAY_NAME,
IS_ENABLED,ACTION_HELP_URL,CONTEXT_ROOT) values (nextval('SEQ_EG_ACTION'),'Populate Structural Classifications',
null,null,current_timestamp,'/common/ajaxCommon-populateStructuralClassifications.action',null,null,(select id_module from eg_module where 
module_name='Existing property'),0,'Populate Structural Classifications',0,null,'ptis');

Insert into eg_action (ID,NAME,ENTITYID,TASKID,UPDATEDTIME,URL,QUERYPARAMS,URLORDERID,MODULE_ID,ORDER_NUMBER,DISPLAY_NAME,
IS_ENABLED,ACTION_HELP_URL,CONTEXT_ROOT) values (nextval('SEQ_EG_ACTION'),'Populate Wards',
null,null,current_timestamp,'/common/ajaxCommon-wardByZone.action',null,null,(select id_module from eg_module where 
module_name='Existing property'),0,'Populate Wards',0,null,'ptis');

Insert into eg_action (ID,NAME,ENTITYID,TASKID,UPDATEDTIME,URL,QUERYPARAMS,URLORDERID,MODULE_ID,ORDER_NUMBER,DISPLAY_NAME,
IS_ENABLED,ACTION_HELP_URL,CONTEXT_ROOT) values (nextval('SEQ_EG_ACTION'),'Populate Streets',
null,null,current_timestamp,'/common/ajaxCommon-streetByWard.action',null,null,(select id_module from eg_module where 
module_name='Existing property'),0,'Populate Streets',0,null,'ptis');

insert into eg_roleaction_map (actionid,roleid) values((select id from eg_action where name='CitizenInboxForm'),
(select id from eg_role where name='assessor'));

insert into eg_roleaction_map (actionid,roleid) values((select id from eg_action where name='LoginForm'),
(select id from eg_role where name='assessor'));

insert into eg_roleaction_map (actionid,roleid) values((select id from eg_action where name='Populate Wards' and context_root='ptis'),
(select id from eg_role where name='assessor'));

insert into eg_roleaction_map (actionid,roleid) values((select id from eg_action where name='Populate LocationFactors' and context_root='ptis'),
(select id from eg_role where name='assessor'));

insert into eg_roleaction_map (actionid,roleid) values((select id from eg_action where name='Populate Structural Classifications' and context_root='ptis'),
(select id from eg_role where name='assessor'));

insert into eg_roleaction_map (actionid,roleid) values((select id from eg_action where name='Populate Streets' and context_root='ptis'),
(select id from eg_role where name='assessor'));

update eg_boundary set boundarynum = id where boundarynum is null and boundarytype in
(select id from eg_boundary_type where name in('Zone','Ward'));

--rollback delete from eg_roleaction_map where actionid in(select id from eg_action where name in ('CitizenInboxForm','LoginForm','Populate Wards','Populate LocationFactors','Populate Structural Classifications','Populate Streets') and context_root='ptis') and roleid in(select id from eg_role where name='assessor');

--rollback delete from eg_action where name in('Populate Wards','Populate LocationFactors','Populate Structural Classifications','Populate Streets') and context_root='ptis';





