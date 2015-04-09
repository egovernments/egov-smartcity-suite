INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'OfficialsHome', null, null, now(), '/controller/home', null, 
null,(Select id_module from eg_module where module_name='egi'), null, 'OfficialsHome', 0, null, 'egi');

INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Inbox', null, null, now(), '/controller/inbox', null, 
null,(Select id_module from eg_module where module_name='egi'), null, 'Inbox', 0, null, 'egi');

INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'InboxDraft', null, null, now(), '/controller/inbox/draft', null, 
null,(Select id_module from eg_module where module_name='egi'), null, 'Inbox Draft', 0, null, 'egi');

INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'InboxHistory', null, null, now(), '/controller/inbox/history', null, 
null,(Select id_module from eg_module where module_name='egi'), null, 'Inbox History', 0, null, 'egi');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/controller/home'),(Select id from eg_role where name='SuperUser'));

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/inbox'),(Select id from eg_role where name='SuperUser'));

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/inbox/draft'),(Select id from eg_role where name='SuperUser'));

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where url='/inbox/history'),(Select id from eg_role where name='SuperUser'));