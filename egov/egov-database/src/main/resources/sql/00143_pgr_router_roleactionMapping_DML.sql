
insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Create Router'),(Select id from eg_role where name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Create Router' and context_root='pgr');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='AjaxRouterComplaintType'),(Select id from eg_role where name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='AjaxRouterComplaintType' and context_root='pgr');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='AjaxRouterBoundariesbyType'),(Select id from eg_role where name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='AjaxRouterBoundariesbyType' and context_root='pgr');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='AjaxRouterPosition'),(Select id from eg_role where name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='AjaxRouterPosition' and context_root='pgr');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Update Router'),(Select id from eg_role where name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Update Router' and context_root='pgr');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Delete Router'),(Select id from eg_role where name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Delete Router' and context_root='pgr');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='View Router'),(Select id from eg_role where name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='View Router' and context_root='pgr');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Search viewRouter Result'),(Select id from eg_role where name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Search viewRouter Result' and context_root='pgr');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='RouterView'),(Select id from eg_role where name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='RouterView' and context_root='pgr');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Edit Router'),(Select id from eg_role where name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Edit Router' and context_root='pgr');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='Search updateRouter Result'),(Select id from eg_role where name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='Search updateRouter Result' and context_root='pgr');

insert into eg_roleaction_map (Actionid,roleid)
values((select id from eg_action where name='update RouterViaSearch'),(Select id from eg_role where name='Super User'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name='update RouterViaSearch' and context_root='pgr');

