INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Update Designation', null, null, now(), '/designation/update', null, 
null,(SELECT id_module FROM eg_module WHERE module_name='Designation'), null, 'Update', 1, null, 'eis');

INSERT INTO eg_roleaction_map (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Update Designation' and context_root='eis'),
(SELECT id FROM eg_role where name='Super User'));

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'load designation', null, null, now(), '/designation/ajax/result', null, 
null,(SELECT id_module FROM eg_module WHERE module_name='Designation'), null, 'load designation', 0, null, 'eis');

INSERT INTO eg_roleaction_map (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='load designation' and context_root='eis'),
(SELECT id FROM eg_role where name='Super User'));

--rollback DELETE FROM eg_roleaction_map where actionid in(SELECT id FROM eg_action WHERE name in('Update Designation','load designation')) and roleid in(SELECT id FROM eg_role WHERE name='Super User');
--rollback DELETE FROM eg_action WHERE name in('Update Designation','load designation') and context_root='eis';