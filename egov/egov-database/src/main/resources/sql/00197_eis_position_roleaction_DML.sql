INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Ajax Call in Search Position', null, null, now(), '/position/resultList-update', 
null, null,(SELECT id_module FROM eg_module WHERE module_name='Position'), null, 'Create', 0, null, 'eis');

INSERT INTO eg_roleaction_map (actionid, roleid) select a.id,(select id FROM eg_role WHERE name='Super User') 
FROM eg_action a WHERE lower(context_root)='eis' and name='Ajax Call in Search Position';


INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Update Position', null, null, now(), '/position-update', 
null, null,(SELECT id_module FROM eg_module WHERE module_name='Position'), null, 'Create', 0, null, 'eis');

INSERT INTO eg_roleaction_map (actionid, roleid) select a.id,(select id FROM eg_role WHERE name='Super User') 
FROM eg_action a WHERE lower(context_root)='eis' and name='Update Position';