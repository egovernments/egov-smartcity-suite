
INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Create Router', null, null, now(), '/router/create', null, 
null,(Select id_module from eg_module where module_name='Pgr Masters'), null, 'Create Router', 1, null, 'pgr');

--rollback delete from eg_action where name='Create Router' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Create Router'),(Select id from eg_role where name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Create Router' and context_root='pgr');

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'AjaxRouterComplaintType', null, null, now(), '/complaint/router/complaintTypes', null, 
null,(Select id_module from eg_module where module_name='Pgr Masters'), null, 'AjaxRouterComplaintType', 0, null, 'pgr');

--rollback delete from eg_action where name='AjaxRouterComplaintType' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='AjaxRouterComplaintType'),(Select id from eg_role where name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='AjaxRouterComplaintType' and context_root='pgr');

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'AjaxRouterBoundariesbyType', null, null, now(), '/complaint/router/boundaries-by-type', null, 
null,(Select id_module from eg_module where module_name='Pgr Masters'), null, 'AjaxRouterBoundariesbyType', 1, null, 'pgr');

--rollback delete from eg_action where name='AjaxRouterBoundariesbyType' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='AjaxRouterBoundariesbyType'),(Select id from eg_role where name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='AjaxRouterBoundariesbyType' and context_root='pgr');

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'AjaxRouterPosition', null, null, now(), '/complaint/router/position', null, 
null,(Select id_module from eg_module where module_name='Pgr Masters'), null, 'AjaxRouterPosition', 1, null, 'pgr');

--rollback delete from eg_action where name='AjaxRouterPosition' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='AjaxRouterPosition'),(Select id from eg_role where name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='AjaxRouterPosition' and context_root='pgr');
