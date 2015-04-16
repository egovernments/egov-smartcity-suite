INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'OfficialsProfileEdit', null, null, now(), '/home/profile/edit', null, 
null,(Select id_module from eg_module where module_name='egi'), null, 'OfficialsProfileEdit', 0, null, 'egi');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where "name"='OfficialsProfileEdit'),(Select id from eg_role where name='SuperUser'));

update eg_user set createdby=1,lastmodifiedby=1;

