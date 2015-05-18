INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Update Router', null, null, now(), '/router/update', null, 
null,(Select id_module from eg_module where module_name='Pgr Masters'), null, 'Update Router', 1, null, 'pgr');

--rollback delete from eg_action where name='Update Router' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Update Router'),(Select id from eg_role where name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Update Router' and context_root='pgr');

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Delete Router', null, null, now(), '/router/delete', null, 
null,(Select id_module from eg_module where module_name='Pgr Masters'), null, 'Delete Router', 1, null, 'pgr');

--rollback delete from eg_action where name='Delete Router' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Delete Router'),(Select id from eg_role where name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Delete Router' and context_root='pgr');
