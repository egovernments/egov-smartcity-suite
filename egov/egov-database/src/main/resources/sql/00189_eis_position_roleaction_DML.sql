INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Search Position', null, null, now(), '/position/search', 
null, null,(SELECT id_module FROM eg_module WHERE module_name='Position'), null, 'Search', 1, null, 'eis');

INSERT INTO eg_roleaction_map (actionid, roleid) select a.id,(select id FROM eg_role WHERE name='Super User') 
FROM eg_action a WHERE lower(context_root)='eis' and name='Search Position';

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Create Position', null, null, now(), '/position/create', 
null, null,(SELECT id_module FROM eg_module WHERE module_name='Position'), null, 'Create', 1, null, 'eis');

INSERT INTO eg_roleaction_map (actionid, roleid) select a.id,(select id FROM eg_role WHERE name='Super User') 
FROM eg_action a WHERE lower(context_root)='eis' and name='Create Position';
