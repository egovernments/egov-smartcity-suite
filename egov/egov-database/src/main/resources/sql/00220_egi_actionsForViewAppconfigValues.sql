
Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,
queryparams,urlorderid,module_id,order_number,display_name,is_enabled,
action_help_url,context_root) values
(nextval('seq_eg_action'),
'viewAppConfig',null,null,now(),'/appConfig/view',
null,null,(SELECT id FROM EG_MODULE WHERE name = 'Configuration'),
2,'View AppConfig','true',null,'egi');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) 
LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'viewAppConfig'));



Insert into EG_ACTION (id,name,entityid,taskid,updatedtime,url,
queryparams,urlorderid,module_id,order_number,display_name,is_enabled,
action_help_url,context_root) values
(nextval('seq_eg_action'),
'viewAppConfigAjaxResult',null,null,now(),'/appConfig/ajax/result',
null,null,(SELECT id FROM EG_MODULE WHERE name = 'Configuration'),
3,'ViewAppConfigAjax','false',null,'egi');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) 
LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'viewAppConfigAjaxResult'));