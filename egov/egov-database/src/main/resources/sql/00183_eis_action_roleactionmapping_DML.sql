INSERT INTO eg_module(id_module,module_name,lastupdatedtimestamp,isenabled,module_namelocal,baseurl,parentid,module_desc,order_num)
VALUES(nextval('SEQ_MODULEMASTER'),'EIS',current_date,1,null,'eis',null,'EIS',1);

INSERT INTO eg_module(id_module,module_name,lastupdatedtimestamp,isenabled,module_namelocal,baseurl,parentid,module_desc,order_num)
VALUES(nextval('SEQ_MODULEMASTER'),'EIS Masters',current_date,1,null,'eis',(SELECT id_module FROM eg_module WHERE module_name='EIS'),'Masters',1);

INSERT INTO eg_module(id_module,module_name,lastupdatedtimestamp,isenabled,module_namelocal,baseurl,parentid,module_desc,order_num)
VALUES(nextval('SEQ_MODULEMASTER'),'Employee',current_date,1,null,'eis',(SELECT id_module FROM eg_module WHERE module_name='EIS Masters'),
'Employee',1);

INSERT INTO eg_module(id_module,module_name,lastupdatedtimestamp,isenabled,module_namelocal,baseurl,parentid,module_desc,order_num)
VALUES(nextval('SEQ_MODULEMASTER'),'Designation',current_date,1,null,'eis',(SELECT id_module FROM eg_module WHERE module_name='EIS Masters'),
'Designation',2);

INSERT INTO eg_module(id_module,module_name,lastupdatedtimestamp,isenabled,module_namelocal,baseurl,parentid,module_desc,order_num)
VALUES(nextval('SEQ_MODULEMASTER'),'Position',current_date,1,null,'eis',(SELECT id_module FROM eg_module WHERE module_name='EIS Masters'),
'Position',3);

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Create Designation', null, null, now(), '/designation/create', 
null, null,(SELECT id_module FROM eg_module WHERE module_name='Designation'), null, 'Create', 1, null, 'eis');

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'View Designation', null, null, now(), '/designation/view', null, 
null,(SELECT id_module FROM eg_module WHERE module_name='Designation'), null, 'View', 1, null, 'eis');

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Create Employee', null, null, now(), '/employee/create', null, 
null,(SELECT id_module FROM eg_module WHERE module_name='Employee'), null, 'Create', 1, null, 'eis');

INSERT INTO eg_roleaction_map (actionid, roleid) select a.id,(select id FROM eg_role WHERE name='Super User') FROM eg_action a WHERE lower(context_root)='eis';


--rollback DELETE FROM eg_module WHERE module_name in('EIS','EIS Masters','Employee','Designation','Position');   
--rollback DELETE FROM eg_roleaction_map where actionid in(SELECT id FROM eg_action where name in ('Create Designation','View Designation','Create Employee') and context_root='eis') and roleid in(SELECT id from eg_role where name='Super User'); 
--rollback DELETE FROM eg_action WHERE name in('Create Designation','View Designation','Create Employee') and context_root='eis';
