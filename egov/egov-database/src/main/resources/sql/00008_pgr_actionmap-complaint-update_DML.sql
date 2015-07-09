
INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Update Complaint', null, null, now(), '/complaint-update', null, 
null,(Select id_module from eg_module where module_name='PGR'), null, 'Update Complaint', 0, null, 'pgr');

--rollback delete from eg_action where url='/complaint-update';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/complaint-update'),(Select id_role from eg_roles where role_name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where url='/complaint-update');



INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'DEFAULT', null, null, now(), '/complaint-update', null, 
null,(Select id_module from eg_module where module_name='PGR'), null, 'DEFAULT', 0, null, 'pgr');


