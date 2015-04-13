INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 																																																																																																																																																								INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'RemoveFavourite', null, null, now(), '/controller/home/remove-favourite', null, 
null,(Select id_module from eg_module where module_name='egi'), null, 'RemoveFavourite', 0, null, 'egi');

INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'AddFavourite', null, null, now(), '/controller/home/add-favourite', null, 
null,(Select id_module from eg_module where module_name='egi'), null, 'AddFavourite', 0, null, 'egi');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/controller/home/remove-favourite'),(Select id from eg_role where name='SuperUser'));

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/controller/home/add-favourite'),(Select id from eg_role where name='SuperUser'));
