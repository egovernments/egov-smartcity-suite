INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'OfficialSentFeedBack', null, null, now(), '/home/feedback/sent', null, 
null,(Select id_module from eg_module where module_name='egi'), null, 'OfficialSentFeedBack', 0, null, 'egi');

INSERT INTO eg_action("id", "name", entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'OfficialChangePassword', null, null, now(), '/home/password/update', null, 
null,(Select id_module from eg_module where module_name='egi'), null, 'OfficialChangePassword', 0, null, 'egi');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where "name"='OfficialSentFeedBack'),(Select id from eg_role where name='Super User'));

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where "name"='OfficialChangePassword'),(Select id from eg_role where name='Super User'));

update eg_action set url='/home/favourite/add' where url='/home/add-favourite';

update eg_action set url='/home/favourite/remove' where url='/home/remove-favourite';