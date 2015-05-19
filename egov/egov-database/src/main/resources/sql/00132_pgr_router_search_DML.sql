
update eg_action set is_enabled=0 where url ='/router/update';
--rollback update eg_action set is_enabled=1 where url ='/router/update';
update eg_action set is_enabled=0 where url ='/router/delete';
--rollback update eg_action set is_enabled=1 where url ='/router/delete';


--View Router
INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'View Router', null, null, now(), '/router/search-view', null, 
null,(Select id_module from eg_module where module_name='Pgr Masters'), null, 'View Router', 1, null, 'pgr');

--rollback delete from eg_action where name='View Router' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='View Router'),(Select id from eg_role where name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='View Router' and context_root='pgr');


INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Search viewRouter Result', null, null, now(), '/router/resultList-view', null, 
null,(Select id_module from eg_module where module_name='Pgr Masters'), null, 'Search viewRouter Result', 0, null, 'pgr');

--rollback delete from eg_action where name='Search viewRouter Result' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Search viewRouter Result'),(Select id from eg_role where name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Search viewRouter Result' and context_root='pgr');


INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'RouterView', null, null, now(), '/router/view', null, 
null,(Select id_module from eg_module where module_name='Pgr Masters'), null, 'RouterView', 0, null, 'pgr');

--rollback delete from eg_action where name='RouterView' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='RouterView'),(Select id from eg_role where name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='RouterView' and context_root='pgr');

--Edit Router 

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Edit Router', null, null, now(), '/router/search-update', null, 
null,(Select id_module from eg_module where module_name='Pgr Masters'), null, 'Edit Router', 1, null, 'pgr');

--rollback delete from eg_action where name='Edit Router' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Edit Router'),(Select id from eg_role where name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Edit Router' and context_root='pgr');


INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'Search updateRouter Result', null, null, now(), '/router/resultList-update', null, 
null,(Select id_module from eg_module where module_name='Pgr Masters'), null, 'Search updateRouter Result', 0, null, 'pgr');

--rollback delete from eg_action where name='Search updateRouter Result' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Search updateRouter Result'),(Select id from eg_role where name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Search updateRouter Result' and context_root='pgr');

INSERT INTO eg_action(id, name, entityid, taskid, updatedtime, url, queryparams, urlorderid, module_id, order_number, display_name, 
is_enabled, action_help_url, context_root) VALUES (nextval('seq_eg_action'), 'update RouterViaSearch', null, null, now(), '/router/update-search', null, 
null,(Select id_module from eg_module where module_name='Pgr Masters'), null, 'update RouterViaSearch', 0, null, 'pgr');

--rollback delete from eg_action where name='update RouterViaSearch' and context_root='pgr';

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='update RouterViaSearch'),(Select id from eg_role where name='SuperUser'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='update RouterViaSearch' and context_root='pgr');


